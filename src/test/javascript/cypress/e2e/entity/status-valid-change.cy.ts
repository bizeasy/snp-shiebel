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

describe('StatusValidChange e2e test', () => {
  const statusValidChangePageUrl = '/status-valid-change';
  let username: string;
  let password: string;
  const statusValidChangeSample = {};

  let statusValidChange;
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
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/statuses',
      body: { name: 'expatiate', sequenceNo: 32411, description: 'ick rationalise uncork' },
    }).then(({ body }) => {
      status = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/status-valid-changes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/status-valid-changes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/status-valid-changes/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/statuses', {
      statusCode: 200,
      body: [status],
    });
  });

  afterEach(() => {
    if (statusValidChange) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/status-valid-changes/${statusValidChange.id}`,
      }).then(() => {
        statusValidChange = undefined;
      });
    }
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

  it('StatusValidChanges menu should load StatusValidChanges page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('status-valid-change');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('StatusValidChange').should('exist');
    cy.location('pathname').should('eq', statusValidChangePageUrl);
  });

  describe('StatusValidChange page', () => {
    it('should have translated page title', () => {
      cy.visit(statusValidChangePageUrl);
      cy.getEntityHeading('StatusValidChange').should('not.contain', 'snpApp.statusValidChange.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(statusValidChangePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create StatusValidChange page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${statusValidChangePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('StatusValidChange');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', statusValidChangePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/status-valid-changes',
          body: {
            ...statusValidChangeSample,
            status,
            statusTo: status,
          },
        }).then(({ body }) => {
          statusValidChange = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/status-valid-changes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [statusValidChange],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(statusValidChangePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details StatusValidChange page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('statusValidChange');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', statusValidChangePageUrl);
      });

      it('edit button click should load edit StatusValidChange page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('StatusValidChange');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', statusValidChangePageUrl);
      });

      it('edit button click should load edit StatusValidChange page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('StatusValidChange');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', statusValidChangePageUrl);
      });

      it('last delete button click should delete instance of StatusValidChange', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('statusValidChange').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', statusValidChangePageUrl);

        statusValidChange = undefined;
      });
    });
  });

  describe('new StatusValidChange page', () => {
    beforeEach(() => {
      cy.visit(statusValidChangePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('StatusValidChange');
    });

    it('should create an instance of StatusValidChange', () => {
      cy.get(`[data-cy="transitionName"]`).type('before because');
      cy.get(`[data-cy="transitionName"]`).should('have.value', 'before because');

      cy.get(`[data-cy="status"]`).select(1);
      cy.get(`[data-cy="statusTo"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        statusValidChange = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', statusValidChangePageUrl);
    });
  });
});
