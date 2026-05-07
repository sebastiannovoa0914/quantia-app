import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule } from '@angular/forms';

// IMPORTACIÓN CLAVE: Importamos el componente desde su archivo local
// Asegúrate de que en 'crear-propiedad.ts' diga: export class CrearPropiedad
import { CrearPropiedad } from './crear-propiedad'; 

describe('CrearPropiedad', () => {
  let component: CrearPropiedad;
  let fixture: ComponentFixture<CrearPropiedad>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      // Declaramos el componente que vamos a probar
      declarations: [ CrearPropiedad ], 
      imports: [
        HttpClientTestingModule,
        RouterTestingModule,
        FormsModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CrearPropiedad);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});