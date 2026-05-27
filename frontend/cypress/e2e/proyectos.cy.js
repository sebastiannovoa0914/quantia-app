describe('Gestión de Proyectos - Quantia Portal', () => {
  
    beforeEach(() => {
      cy.visit('/login');
      // Usamos tus credenciales confirmadas
      cy.get('input[name="email"]').type('nuevo_intentofinal_01@quantia.com');
      cy.get('input[name="contrasena"]').type('clave12');
      cy.get('button[type="submit"]').click();
      cy.url().should('include', '/home');
    });
  
    it('Debe registrar una propiedad con fechas y ubicación', () => {
      // 1. Entrar al formulario
      cy.contains(/crear proyecto/i).click();
  
      // 2. Llenar campos según el HTML
      cy.get('input[name="nombre"]').type('Lote San Jorge Sector A');
      
      // Formato de fecha para input type="date" es YYYY-MM-DD
      cy.get('input[name="fecha_inicio"]').type('2026-05-10');
      cy.get('input[name="fecha_fin"]').type('2026-05-11');
      
      cy.get('textarea[name="descripcion"]').type('Ubicación privilegiada en Fusagasugá, ideal para desarrollo técnico.');
  
      // 3. Mapa
      cy.get('#map').should('be.visible').wait(1000);
      cy.get('#map').click('center'); // Simula definir la ubicación
  
      // 4. Guardar
      // El botón se habilita solo si el form es válido
      cy.get('button[type="submit"]').should('not.be.disabled').click();

      // --- NUEVO PASO: Manejar el mensaje de éxito ---
  // Buscamos el botón "OK" del modal que aparece en la imagen
  cy.contains('button', 'OK').should('be.visible').click();
  
      // 5. Verificación
      cy.url().should('include', '/home');
      cy.contains('Lote San Jorge Sector A').should('be.visible');
    });
  });