export interface BeneficioResponseDTO {
  id: number;
  nome: string;
  valor: number;
}

export interface BeneficioRequestDTO {
  nome: string;
  descricao?: string;
  valor: number;
}

export interface TransferRequestDTO {
  fromId: number;
  toId: number;
  amount: number;
}

export interface Toast {
  id: number;
  type: 'success' | 'error' | 'info';
  message: string;
}
