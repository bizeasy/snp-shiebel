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

describe('QuoteAdjustment e2e test', () => {
  const quoteAdjustmentPageUrl = '/quote-adjustment';
  let username: string;
  let password: string;
  const quoteAdjustmentSample = {};

  let quoteAdjustment;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/quote-adjustments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/quote-adjustments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/quote-adjustments/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (quoteAdjustment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/quote-adjustments/${quoteAdjustment.id}`,
      }).then(() => {
        quoteAdjustment = undefined;
      });
    }
  });

  it('QuoteAdjustments menu should load QuoteAdjustments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('quote-adjustment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('QuoteAdjustment').should('exist');
    cy.location('pathname').should('eq', quoteAdjustmentPageUrl);
  });

  describe('QuoteAdjustment page', () => {
    it('should have translated page title', () => {
      cy.visit(quoteAdjustmentPageUrl);
      cy.getEntityHeading('QuoteAdjustment').should('not.contain', 'snpApp.quoteAdjustment.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(quoteAdjustmentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create QuoteAdjustment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${quoteAdjustmentPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('QuoteAdjustment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quoteAdjustmentPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/quote-adjustments',
          body: quoteAdjustmentSample,
        }).then(({ body }) => {
          quoteAdjustment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/quote-adjustments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/quote-adjustments?page=0&size=20>; rel="last",<http://localhost/api/quote-adjustments?page=0&size=20>; rel="first"',
              },
              body: [quoteAdjustment],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(quoteAdjustmentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details QuoteAdjustment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('quoteAdjustment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quoteAdjustmentPageUrl);
      });

      it('edit button click should load edit QuoteAdjustment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('QuoteAdjustment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quoteAdjustmentPageUrl);
      });

      it('edit button click should load edit QuoteAdjustment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('QuoteAdjustment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quoteAdjustmentPageUrl);
      });

      it('last delete button click should delete instance of QuoteAdjustment', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('quoteAdjustment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quoteAdjustmentPageUrl);

        quoteAdjustment = undefined;
      });
    });
  });

  describe('new QuoteAdjustment page', () => {
    beforeEach(() => {
      cy.visit(quoteAdjustmentPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('QuoteAdjustment');
    });

    it('should create an instance of QuoteAdjustment', () => {
      cy.get(`[data-cy="description"]`).type('smoke');
      cy.get(`[data-cy="description"]`).should('have.value', 'smoke');

      cy.get(`[data-cy="comments"]`).type('at hyena');
      cy.get(`[data-cy="comments"]`).should('have.value', 'at hyena');

      cy.get(`[data-cy="amount"]`).type('21208.99');
      cy.get(`[data-cy="amount"]`).should('have.value', '21208.99');

      cy.get(`[data-cy="sourcePercentage"]`).type('16093.57');
      cy.get(`[data-cy="sourcePercentage"]`).should('have.value', '16093.57');

      cy.get(`[data-cy="sourceRefId"]`).type('fooey gadzooks midwife');
      cy.get(`[data-cy="sourceRefId"]`).should('have.value', 'fooey gadzooks midwife');

      cy.get(`[data-cy="exemptAmount"]`).type('14067.62');
      cy.get(`[data-cy="exemptAmount"]`).should('have.value', '14067.62');

      cy.get(`[data-cy="includeInTax"]`).should('not.be.checked');
      cy.get(`[data-cy="includeInTax"]`).click();
      cy.get(`[data-cy="includeInTax"]`).should('be.checked');

      cy.get(`[data-cy="includeInShipping"]`).should('not.be.checked');
      cy.get(`[data-cy="includeInShipping"]`).click();
      cy.get(`[data-cy="includeInShipping"]`).should('be.checked');

      cy.get(`[data-cy="includedInPrice"]`).should('not.be.checked');
      cy.get(`[data-cy="includedInPrice"]`).click();
      cy.get(`[data-cy="includedInPrice"]`).should('be.checked');

      cy.get(`[data-cy="entryDate"]`).type('2023-12-14T05:03');
      cy.get(`[data-cy="entryDate"]`).blur();
      cy.get(`[data-cy="entryDate"]`).should('have.value', '2023-12-14T05:03');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        quoteAdjustment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', quoteAdjustmentPageUrl);
    });
  });
});
