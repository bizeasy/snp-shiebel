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

describe('Lot e2e test', () => {
  const lotPageUrl = '/lot';
  let username: string;
  let password: string;
  const lotSample = {};

  let lot;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/lots+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/lots').as('postEntityRequest');
    cy.intercept('DELETE', '/api/lots/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (lot) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/lots/${lot.id}`,
      }).then(() => {
        lot = undefined;
      });
    }
  });

  it('Lots menu should load Lots page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('lot');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Lot').should('exist');
    cy.location('pathname').should('eq', lotPageUrl);
  });

  describe('Lot page', () => {
    it('should have translated page title', () => {
      cy.visit(lotPageUrl);
      cy.getEntityHeading('Lot').should('not.contain', 'snpApp.lot.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(lotPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Lot page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${lotPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Lot');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', lotPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/lots',
          body: lotSample,
        }).then(({ body }) => {
          lot = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/lots+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [lot],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(lotPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Lot page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('lot');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', lotPageUrl);
      });

      it('edit button click should load edit Lot page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Lot');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', lotPageUrl);
      });

      it('edit button click should load edit Lot page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Lot');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', lotPageUrl);
      });

      it('last delete button click should delete instance of Lot', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('lot').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', lotPageUrl);

        lot = undefined;
      });
    });
  });

  describe('new Lot page', () => {
    beforeEach(() => {
      cy.visit(lotPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Lot');
    });

    it('should create an instance of Lot', () => {
      cy.get(`[data-cy="creationDate"]`).type('2023-12-14T07:50');
      cy.get(`[data-cy="creationDate"]`).blur();
      cy.get(`[data-cy="creationDate"]`).should('have.value', '2023-12-14T07:50');

      cy.get(`[data-cy="quantity"]`).type('28677.3');
      cy.get(`[data-cy="quantity"]`).should('have.value', '28677.3');

      cy.get(`[data-cy="expirationDate"]`).type('2023-12-14T13:50');
      cy.get(`[data-cy="expirationDate"]`).blur();
      cy.get(`[data-cy="expirationDate"]`).should('have.value', '2023-12-14T13:50');

      cy.get(`[data-cy="retestDate"]`).type('2023-12-14T13:57');
      cy.get(`[data-cy="retestDate"]`).blur();
      cy.get(`[data-cy="retestDate"]`).should('have.value', '2023-12-14T13:57');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        lot = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', lotPageUrl);
    });
  });
});
