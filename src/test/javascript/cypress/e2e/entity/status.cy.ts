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

describe('Status e2e test', () => {
  const statusPageUrl = '/status';
  let username: string;
  let password: string;
  const statusSample = { name: 'vestment broadcast' };

  let status;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/statuses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/statuses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/statuses/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (status) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/statuses/${status.id}`,
      }).then(() => {
        status = undefined;
      });
    }
  });

  it('Statuses menu should load Statuses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('status');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Status').should('exist');
    cy.location('pathname').should('eq', statusPageUrl);
  });

  describe('Status page', () => {
    it('should have translated page title', () => {
      cy.visit(statusPageUrl);
      cy.getEntityHeading('Status').should('not.contain', 'snpApp.status.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(statusPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Status page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${statusPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Status');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', statusPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/statuses',
          body: statusSample,
        }).then(({ body }) => {
          status = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/statuses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [status],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(statusPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Status page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('status');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', statusPageUrl);
      });

      it('edit button click should load edit Status page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Status');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', statusPageUrl);
      });

      it('edit button click should load edit Status page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Status');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', statusPageUrl);
      });

      it('last delete button click should delete instance of Status', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('status').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', statusPageUrl);

        status = undefined;
      });
    });
  });

  describe('new Status page', () => {
    beforeEach(() => {
      cy.visit(statusPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Status');
    });

    it('should create an instance of Status', () => {
      cy.get(`[data-cy="name"]`).type('while hm boo');
      cy.get(`[data-cy="name"]`).should('have.value', 'while hm boo');

      cy.get(`[data-cy="sequenceNo"]`).type('29443');
      cy.get(`[data-cy="sequenceNo"]`).should('have.value', '29443');

      cy.get(`[data-cy="description"]`).type('fen commonly');
      cy.get(`[data-cy="description"]`).should('have.value', 'fen commonly');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        status = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', statusPageUrl);
    });
  });
});
