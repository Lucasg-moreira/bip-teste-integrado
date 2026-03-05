import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastService } from '../../../services/toast.service';

@Component({
  selector: 'app-toast-container',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="toast-container">
      @for (toast of toastService.toasts(); track toast.id) {
        <div class="toast toast-{{ toast.type }}" (click)="toastService.remove(toast.id)">
          <span class="toast-icon">
            @if (toast.type === 'success') { ✓ }
            @if (toast.type === 'error') { ✕ }
            @if (toast.type === 'info') { ℹ }
          </span>
          <span>{{ toast.message }}</span>
        </div>
      }
    </div>
  `,
  styles: [`
    .toast-icon {
      font-size: 14px;
      font-weight: 700;
      flex-shrink: 0;
    }
    .toast { cursor: pointer; }
  `]
})
export class ToastContainerComponent {
  toastService = inject(ToastService);
}
