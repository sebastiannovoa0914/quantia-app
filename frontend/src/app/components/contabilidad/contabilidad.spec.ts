import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router'; // Añadido para que no falle por el RouterModule

// 1. Corregimos la ruta del import (apuntando al nuevo nombre del archivo)
// 2. Cambiamos 'Contabilidad' por el nombre real de la clase: 'ContabilidadComponent'
import { ContabilidadComponent } from './contabilidad.component';

describe('ContabilidadComponent', () => {
  let component: ContabilidadComponent;
  let fixture: ComponentFixture<ContabilidadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContabilidadComponent],
      providers: [
        provideRouter([]) // Esto evita que el test falle al inicializar el RouterModule en la vista
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ContabilidadComponent);
    component = fixture.componentInstance;
    
    // Forzamos la detección de cambios inicial para procesar el HTML dinámico
    fixture.detectChanges(); 
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});