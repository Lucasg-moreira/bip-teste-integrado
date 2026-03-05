import { Component, EventEmitter, Input, OnChanges, Output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { BeneficioRequestDTO, BeneficioResponseDTO } from '../../models/beneficio.model';
import { BeneficioService } from '../../services/beneficio.service';
import { ToastService } from '../../services/toast.service';

@Component({
  selector: 'app-beneficio-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './beneficio-form.component.html',
  styleUrls: ['./beneficio-form.component.scss']
})
export class BeneficioFormComponent implements OnChanges {
  @Input() beneficio: BeneficioResponseDTO | null = null;
  @Output() saved = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  private fb = inject(FormBuilder);
  private beneficioService = inject(BeneficioService);
  private toastService = inject(ToastService);

  loading = false;

  form = this.fb.group({
    nome: ['', [Validators.required, Validators.minLength(2)]],
    descricao: [''],
    valor: [null as number | null, [Validators.required, Validators.min(0.01)]],
    ativo: [true]
  });

  get isEdit(): boolean {
    return !!this.beneficio;
  }

  ngOnChanges(): void {
    if (this.beneficio) {
      this.form.patchValue({
        nome: this.beneficio.nome,
        valor: this.beneficio.valor,
        ativo: this.beneficio.ativo
      });
    } else {
      this.form.reset();
    }
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    const dto: BeneficioRequestDTO = {
      nome: this.form.value.nome!,
      descricao: this.form.value.descricao || undefined,
      valor: this.form.value.valor!,
      ativo: this.form.value.ativo ?? true
    };

    const request = this.isEdit
      ? this.beneficioService.atualizar(this.beneficio!.id, dto)
      : this.beneficioService.criar(dto);

    request.subscribe({
      next: () => {
        this.toastService.success(
          this.isEdit ? 'Benefício atualizado com sucesso!' : 'Benefício criado com sucesso!'
        );
        this.form.reset();
        this.loading = false;
        this.saved.emit();
      },
      error: (err) => {
        this.loading = false;
        const msg = err?.error?.message || 'Erro ao salvar benefício.';
        this.toastService.error(msg);
      }
    });
  }

  hasError(field: string, error: string): boolean {
    const ctrl = this.form.get(field);
    return !!(ctrl?.hasError(error) && ctrl?.touched);
  }
}
