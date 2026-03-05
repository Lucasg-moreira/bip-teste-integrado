import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BeneficioRequestDTO, BeneficioResponseDTO, TransferRequestDTO } from '../models/beneficio.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BeneficioService {
  private readonly url = environment.apiUrl + '/beneficios';

  constructor(private http: HttpClient) {}

  listarTodos(): Observable<BeneficioResponseDTO[]> {
    return this.http.get<BeneficioResponseDTO[]>(this.url);
  }

  criar(dto: BeneficioRequestDTO): Observable<BeneficioResponseDTO> {
    return this.http.post<BeneficioResponseDTO>(this.url, dto);
  }

  atualizar(id: number, dto: BeneficioRequestDTO): Observable<BeneficioResponseDTO> {
    return this.http.put<BeneficioResponseDTO>(`${this.url}/${id}`, dto);
  }

  transferir(dto: TransferRequestDTO): Observable<void> {
    return this.http.post<void>(`${this.url}/transferencia`, dto);
  }

  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
