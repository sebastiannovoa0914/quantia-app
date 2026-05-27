import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'; // 👈 Necesario para hacer peticiones al backend
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ContabilidadService {

  // Ruta base de tu API en Spring Boot (ajusta el puerto si usas otro diferente al 8080)
  private apiUrl = 'http://localhost:8080/api/contabilidad'; 

  constructor(private http: HttpClient) { }

  // 1. Obtener el historial de transacciones (Sirve global o filtrado por proyecto)
  obtenerTransacciones(proyectoId?: number): Observable<any[]> {
    const url = proyectoId ? `${this.apiUrl}/transacciones?proyectoId=${proyectoId}` : `${this.apiUrl}/transacciones`;
    return this.http.get<any[]>(url);
  }

  // 2. Obtener los totales acumulados (Ingresos, Egresos, Balance) para las tarjetas
  obtenerResumen(proyectoId?: number): Observable<any> {
    const url = proyectoId ? `${this.apiUrl}/resumen?proyectoId=${proyectoId}` : `${this.apiUrl}/resumen`;
    return this.http.get<any>(url);
  }

  // 3. Enviar el formulario del modal al backend para registrar un nuevo movimiento
  registrarMovimiento(movimiento: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/registrar`, movimiento);
  }
}