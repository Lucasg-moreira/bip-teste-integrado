import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BeneficioResponseDTO } from '../../models/beneficio.model';
import { BeneficioService } from '../../services/beneficio.service';
import { ToastService } from '../../services/toast.service';
import { BeneficioFormComponent } from '../beneficio-form/beneficio-form.component';
import { TransferenciaComponent } from '../transferencia/transferencia.component';

@Component({
  selector: 'app-beneficio-list',
  standalone: true,
  imports: [CommonModule, BeneficioFormComponent, TransferenciaComponent],
  templateUrl: './beneficio-list.component.html',
  styleUrls: ['./beneficio-list.component.scss']
})
export class BeneficioListComponent implements OnInit {
  private beneficioService = inject(BeneficioService);
  private toastService = inject(ToastService);

  beneficios: BeneficioResponseDTO[] = [];
  loading = false;

  showFormModal = false;
  showTransferenciaModal = false;
  selectedBeneficio: BeneficioResponseDTO | null = null;

  ngOnInit(): void {
    this.carregarBeneficios();
  }

  carregarBeneficios(): void {
    this.loading = true;
    this.beneficioService.listarTodos().subscribe({
      next: (data) => {
        this.beneficios = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.toastService.error('Erro ao carregar benefícios.');
      }
    });
  }

  abrirCriar(): void {
    this.selectedBeneficio = null;
    this.showFormModal = true;
  }

  abrirEditar(beneficio: BeneficioResponseDTO): void {
    this.selectedBeneficio = beneficio;
    this.showFormModal = true;
  }

  abrirTransferencia(): void {
    this.showTransferenciaModal = true;
  }

  onFormSaved(): void {
    this.showFormModal = false;
    this.carregarBeneficios();
  }

  onTransferred(): void {
    this.showTransferenciaModal = false;
    this.carregarBeneficios();
  }

  get totalSaldo(): number {
    return this.beneficios.reduce((acc, b) => acc + b.valor, 0);
  }
}
