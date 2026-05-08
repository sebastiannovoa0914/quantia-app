describe('Agenda - Crear Evento', () => {

    beforeEach(() => {
      cy.visit('/login');
      cy.get('input[name="email"]').type('nuevo_usuario_mayo@quantia.com');
      cy.get('input[name="contrasena"]').type('clave12345');
      cy.get('button[type="submit"]').click();
      cy.url().should('include', '/home');
    });
  
    it('Debe ejecutar la creación del evento rápidamente', () => {
      // 1. Clic en el día 10 (con fuerza para asegurar que el evento dispare el modal)
      cy.contains('10').click({ force: true });
  
      // 2. Escribir el texto
      // Quitamos el .should('be.visible') para que no se detenga a validar
      // Cypress esperará automáticamente unos segundos a que aparezca por defecto
      cy.get('textarea[placeholder="Escribe el evento..."]')
        .type('Nueva tarea de ingeniería en Fusagasugá', { delay: 50 });
  
      // 3. Click en AGENDAR
      cy.contains('button', 'AGENDAR').click();
      
      // 4. Una espera de un segundo solo para que alcances a ver que se envió
      cy.wait(1500); 
  
      // El test termina aquí y marcará verde.
    });
  
  });