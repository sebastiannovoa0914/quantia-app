import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrearPropiedad } from './crear-propiedad';

describe('CrearPropiedad', () => {
  let component: CrearPropiedad;
  let fixture: ComponentFixture<CrearPropiedad>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CrearPropiedad],
    }).compileComponents();

    fixture = TestBed.createComponent(CrearPropiedad);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
