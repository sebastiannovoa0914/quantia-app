import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AgendaService {
  private apiUrl = 'http://localhost:8080/api/agenda';

  constructor(private http: HttpClient) {}

  listarPorUsuario(idUsuario: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${idUsuario}`);
  }

  guardar(evento: any): Observable<any> {
    return this.http.post(this.apiUrl, evento);
  }

  eliminar(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}