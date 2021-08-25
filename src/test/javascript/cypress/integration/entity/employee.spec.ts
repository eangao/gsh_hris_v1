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

    cy.get(`[data-cy="firstName"]`).type('Jasper').should('have.value', 'Jasper');

    cy.get(`[data-cy="middleName"]`).type('Connecticut blockchains').should('have.value', 'Connecticut blockchains');

    cy.get(`[data-cy="lastName"]`).type('Kemmer').should('have.value', 'Kemmer');

    cy.get(`[data-cy="nameSuffix"]`).type('Generic open').should('have.value', 'Generic open');

    cy.get(`[data-cy="birthdate"]`).type('2021-08-23').should('have.value', '2021-08-23');

    cy.get(`[data-cy="mobileNumber"]`).type('Developer').should('have.value', 'Developer');

    cy.get(`[data-cy="isNotLocked"]`).should('not.be.checked');
    cy.get(`[data-cy="isNotLocked"]`).click().should('be.checked');

    cy.get(`[data-cy="dateHired"]`).type('2021-08-23').should('have.value', '2021-08-23');

    cy.get(`[data-cy="dateDeno"]`).type('2021-08-23').should('have.value', '2021-08-23');

    cy.get(`[data-cy="sickLeaveYearlyCredit"]`).type('20948').should('have.value', '20948');

    cy.get(`[data-cy="sickLeaveYearlyCreditUsed"]`).type('47924').should('have.value', '47924');

    cy.get(`[data-cy="leaveYearlyCredit"]`).type('22083').should('have.value', '22083');

    cy.get(`[data-cy="leaveYearlyCreditUsed"]`).type('8584').should('have.value', '8584');

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
