import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing'; // Necesario para servicios que usan HttpClient
import { ProyectoService } from './propiedad'; // Importamos la clase correcta desde el archivo propiedad.ts

describe('ProyectoService', () => {
  let service: ProyectoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // Agregamos esto para que no falle por el HttpClient
      providers: [ProyectoService]
    });
    service = TestBed.inject(ProyectoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});