import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { Credito } from '../models/credito.model';

@Injectable({
  providedIn: 'root'
})
export class CreditoService {

  private readonly apiUrl = `${environment.apiUrl}/api/creditos`;

  constructor(private http: HttpClient) { }

  findByNumeroNfse(numeroNfse: string): Observable<Credito[]> {
    return this.http.get<Credito[]>(`${this.apiUrl}/${numeroNfse}`)
      .pipe(catchError(this.handleError));
  }

  findByNumeroCredito(numeroCredito: string): Observable<Credito> {
    return this.http.get<Credito>(`${this.apiUrl}/credito/${numeroCredito}`)
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Ocorreu um erro desconhecido';

    if (error.status === 0) {
      errorMessage = 'Erro de conexão. Verifique se o servidor está disponível.';
    } else if (error.status === 404) {
      errorMessage = error.error?.message || 'Nenhum registro encontrado.';
    } else if (error.status >= 500) {
      errorMessage = 'Erro interno do servidor. Tente novamente mais tarde.';
    }

    return throwError(() => new Error(errorMessage));
  }
}
