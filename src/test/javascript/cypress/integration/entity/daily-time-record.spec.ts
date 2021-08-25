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

describe('DailyTimeRecord e2e test', () => {
  const dailyTimeRecordPageUrl = '/daily-time-record';
  const dailyTimeRecordPageUrlPattern = new RegExp('/daily-time-record(\\?.*)?$');
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
    cy.intercept('GET', '/api/daily-time-records+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/daily-time-records').as('postEntityRequest');
    cy.intercept('DELETE', '/api/daily-time-records/*').as('deleteEntityRequest');
  });

  it('should load DailyTimeRecords', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('daily-time-record');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DailyTimeRecord').should('exist');
    cy.url().should('match', dailyTimeRecordPageUrlPattern);
  });

  it('should load details DailyTimeRecord page', function () {
    cy.visit(dailyTimeRecordPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('dailyTimeRecord');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', dailyTimeRecordPageUrlPattern);
  });

  it('should load create DailyTimeRecord page', () => {
    cy.visit(dailyTimeRecordPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('DailyTimeRecord');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', dailyTimeRecordPageUrlPattern);
  });

  it('should load edit DailyTimeRecord page', function () {
    cy.visit(dailyTimeRecordPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('DailyTimeRecord');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', dailyTimeRecordPageUrlPattern);
  });

  it('should create an instance of DailyTimeRecord', () => {
    cy.visit(dailyTimeRecordPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('DailyTimeRecord');

    cy.get(`[data-cy="dateTimeIn"]`).type('2021-08-23T08:32').should('have.value', '2021-08-23T08:32');

    cy.get(`[data-cy="dateTimeOut"]`).type('2021-08-24T04:46').should('have.value', '2021-08-24T04:46');

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
    cy.url().should('match', dailyTimeRecordPageUrlPattern);
  });

  it('should delete last instance of DailyTimeRecord', function () {
    cy.visit(dailyTimeRecordPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('dailyTimeRecord').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dailyTimeRecordPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
