import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BeneficioRequestDTO, BeneficioResponseDTO, TransferRequestDTO } from '../models/beneficio.model';

@Injectable({
  providedIn: 'root'
})
export class BeneficioService {
  private readonly baseUrl = 'http://localhost:8081/api/v1/beneficios';

  constructor(private http: HttpClient) {}

  listarTodos(): Observable<BeneficioResponseDTO[]> {
    return this.http.get<BeneficioResponseDTO[]>(this.baseUrl);
  }

  criar(dto: BeneficioRequestDTO): Observable<BeneficioResponseDTO> {
    return this.http.post<BeneficioResponseDTO>(this.baseUrl, dto);
  }

  atualizar(id: number, dto: BeneficioRequestDTO): Observable<BeneficioResponseDTO> {
    return this.http.put<BeneficioResponseDTO>(`${this.baseUrl}/${id}`, dto);
  }

  transferir(dto: TransferRequestDTO): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/transferencia`, dto);
  }
}
