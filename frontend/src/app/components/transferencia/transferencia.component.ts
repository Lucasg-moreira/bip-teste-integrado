import { Component, EventEmitter, Input, OnChanges, Output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { BeneficioResponseDTO } from '../../models/beneficio.model';
import { BeneficioService } from '../../services/beneficio.service';
import { ToastService } from '../../services/toast.service';

@Component({
  selector: 'app-transferencia',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './transferencia.component.html',
  styleUrls: ['./transferencia.component.scss']
})
export class TransferenciaComponent implements OnChanges {
  @Input() beneficios: BeneficioResponseDTO[] = [];
  @Output() transferred = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  private fb = inject(FormBuilder);
  private beneficioService = inject(BeneficioService);
  private toastService = inject(ToastService);

  loading = false;

  form = this.fb.group({
    fromId: [null as number | null, Validators.required],
    toId: [null as number | null, Validators.required],
    amount: [null as number | null, [Validators.required, Validators.min(0.01)]]
  });

  ngOnChanges(): void {
    this.form.reset();
  }

  get fromBeneficio(): BeneficioResponseDTO | undefined {
    const id = this.form.value.fromId;
    return this.beneficios.find(b => b.id == id);
  }

  get toBeneficio(): BeneficioResponseDTO | undefined {
    const id = this.form.value.toId;
    return this.beneficios.find(b => b.id == id);
  }

  get availableTargets(): BeneficioResponseDTO[] {
    const fromId = this.form.value.fromId;
    return this.beneficios.filter(b => b.id != fromId);
  }

  get availableSources(): BeneficioResponseDTO[] {
    const toId = this.form.value.toId;
    return this.beneficios.filter(b => b.id != toId);
  }

  hasError(field: string, error: string): boolean {
    const ctrl = this.form.get(field);
    return !!(ctrl?.hasError(error) && ctrl?.touched);
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const { fromId, toId, amount } = this.form.value;

    if (fromId === toId) {
      this.toastService.error('Origem e destino não podem ser iguais.');
      return;
    }

    this.loading = true;

    this.beneficioService.transferir({ fromId: fromId!, toId: toId!, amount: amount! }).subscribe({
      next: () => {
        this.toastService.success('Transferência realizada com sucesso!');
        this.form.reset();
        this.loading = false;
        this.transferred.emit();
      },
      error: (err) => {
        this.loading = false;
        const msg = err?.error?.message || 'Erro ao realizar transferência.';
        this.toastService.error(msg);
      }
    });
  }
}
