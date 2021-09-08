import {entityItemSelector} from '../../support/commands';
import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('Employee e2e test', () => {
  const employeePageUrl = '/employee';
  const employeePageUrlPattern = new RegExp('/employee(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/employees+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/employees').as('postEntityRequest');
    cy.intercept('DELETE', '/api/employees/*').as('deleteEntityRequest');
  });

  it('should load Employees', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('employee');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Employee').should('exist');
    cy.url().should('match', employeePageUrlPattern);
  });

  it('should load details Employee page', function () {
    cy.visit(employeePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('employee');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', employeePageUrlPattern);
  });

  it('should load create Employee page', () => {
    cy.visit(employeePageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Employee');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', employeePageUrlPattern);
  });

  it('should load edit Employee page', function () {
    cy.visit(employeePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('Employee');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', employeePageUrlPattern);
  });

  it.skip('should create an instance of Employee', () => {
    cy.visit(employeePageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Employee');

    cy.get(`[data-cy="employeeId"]`).type('99355').should('have.value', '99355');

    cy.get(`[data-cy="username"]`).type('Borders Lane').should('have.value', 'Borders Lane');

    cy.get(`[data-cy="firstName"]`).type('Dedrick').should('have.value', 'Dedrick');

    cy.get(`[data-cy="middleName"]`).type('markets Principal').should('have.value', 'markets Principal');

    cy.get(`[data-cy="lastName"]`).type('Gutkowski').should('have.value', 'Gutkowski');

    cy.get(`[data-cy="nameSuffix"]`).type('User-friendly D').should('have.value', 'User-friendly D');

    cy.get(`[data-cy="birthdate"]`).type('2021-08-24').should('have.value', '2021-08-24');

    cy.get(`[data-cy="sex"]`).should('not.be.checked');
    cy.get(`[data-cy="sex"]`).click().should('be.checked');

    cy.get(`[data-cy="mobileNumber"]`).type('Handmade').should('have.value', 'Handmade');

    cy.get(`[data-cy="email"]`).type('Natalie_Huels@yahoo.com').should('have.value', 'Natalie_Huels@yahoo.com');

    cy.get(`[data-cy="isNotLocked"]`).should('not.be.checked');
    cy.get(`[data-cy="isNotLocked"]`).click().should('be.checked');

    cy.get(`[data-cy="dateHired"]`).type('2021-08-23').should('have.value', '2021-08-23');

    cy.get(`[data-cy="dateDeno"]`).type('2021-08-23').should('have.value', '2021-08-23');

    cy.get(`[data-cy="sickLeaveYearlyCredit"]`).type('55992').should('have.value', '55992');

    cy.get(`[data-cy="sickLeaveYearlyCreditUsed"]`).type('33081').should('have.value', '33081');

    cy.get(`[data-cy="leaveYearlyCredit"]`).type('29104').should('have.value', '29104');

    cy.get(`[data-cy="leaveYearlyCreditUsed"]`).type('94330').should('have.value', '94330');

    cy.setFieldSelectToLastOfEntity('user');

    cy.setFieldSelectToLastOfEntity('department');

    cy.setFieldSelectToLastOfEntity('employmentType');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', employeePageUrlPattern);
  });

  it.skip('should delete last instance of Employee', function () {
    cy.visit(employeePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('employee').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeePageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
