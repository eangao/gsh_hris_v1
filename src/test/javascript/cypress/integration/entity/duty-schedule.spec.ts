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

describe('DutySchedule e2e test', () => {
  const dutySchedulePageUrl = '/duty-schedule';
  const dutySchedulePageUrlPattern = new RegExp('/duty-schedule(\\?.*)?$');
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
    cy.intercept('GET', '/api/duty-schedules+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/duty-schedules').as('postEntityRequest');
    cy.intercept('DELETE', '/api/duty-schedules/*').as('deleteEntityRequest');
  });

  it('should load DutySchedules', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('duty-schedule');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DutySchedule').should('exist');
    cy.url().should('match', dutySchedulePageUrlPattern);
  });

  it('should load details DutySchedule page', function () {
    cy.visit(dutySchedulePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('dutySchedule');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', dutySchedulePageUrlPattern);
  });

  it('should load create DutySchedule page', () => {
    cy.visit(dutySchedulePageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('DutySchedule');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', dutySchedulePageUrlPattern);
  });

  it('should load edit DutySchedule page', function () {
    cy.visit(dutySchedulePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('DutySchedule');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', dutySchedulePageUrlPattern);
  });

  it('should create an instance of DutySchedule', () => {
    cy.visit(dutySchedulePageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('DutySchedule');

    cy.get(`[data-cy="dateTimeIn"]`).type('2021-08-24T02:53').should('have.value', '2021-08-24T02:53');

    cy.get(`[data-cy="dateTimeOut"]`).type('2021-08-23T22:38').should('have.value', '2021-08-23T22:38');

    cy.setFieldSelectToLastOfEntity('employee');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', dutySchedulePageUrlPattern);
  });

  it('should delete last instance of DutySchedule', function () {
    cy.visit(dutySchedulePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('dutySchedule').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dutySchedulePageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
