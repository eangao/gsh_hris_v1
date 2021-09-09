import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Leave e2e test', () => {
  const leavePageUrl = '/leave';
  const leavePageUrlPattern = new RegExp('/leave(\\?.*)?$');
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
    cy.intercept('GET', '/api/leaves+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/leaves').as('postEntityRequest');
    cy.intercept('DELETE', '/api/leaves/*').as('deleteEntityRequest');
  });

  it('should load Leaves', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('leave');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Leave').should('exist');
    cy.url().should('match', leavePageUrlPattern);
  });

  it('should load details Leave page', function () {
    cy.visit(leavePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('leave');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', leavePageUrlPattern);
  });

  it('should load create Leave page', () => {
    cy.visit(leavePageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Leave');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', leavePageUrlPattern);
  });

  it('should load edit Leave page', function () {
    cy.visit(leavePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('Leave');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', leavePageUrlPattern);
  });

  it('should create an instance of Leave', () => {
    cy.visit(leavePageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Leave');

    cy.get(`[data-cy="dateApply"]`).type('2021-08-23').should('have.value', '2021-08-23');

    cy.get(`[data-cy="dateStart"]`).type('2021-08-23').should('have.value', '2021-08-23');

    cy.get(`[data-cy="dateEnd"]`).type('2021-08-23').should('have.value', '2021-08-23');

    cy.get(`[data-cy="dateReturn"]`).type('2021-08-23').should('have.value', '2021-08-23');

    cy.get(`[data-cy="checkupDate"]`).type('2021-08-23').should('have.value', '2021-08-23');

    cy.get(`[data-cy="convalescingPeriod"]`).type('49823').should('have.value', '49823');

    cy.get(`[data-cy="diagnosis"]`)
      .type('../fake-data/blob/hipster.txt')
      .invoke('val')
      .should('match', new RegExp('../fake-data/blob/hipster.txt'));

    cy.get(`[data-cy="physician"]`).type('synthesize').should('have.value', 'synthesize');

    cy.setFieldSelectToLastOfEntity('employee');

    cy.setFieldSelectToLastOfEntity('leaveType');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', leavePageUrlPattern);
  });

  it('should delete last instance of Leave', function () {
    cy.visit(leavePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('leave').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', leavePageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
