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

describe('QuoteItem e2e test', () => {
  const quoteItemPageUrl = '/quote-item';
  let username: string;
  let password: string;
  // const quoteItemSample = {"quantity":17470.94};

  let quoteItem;
  // let quote;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/quotes',
      body: {"name":"hm vaguely community","offerNo":"jell dowse","issueDate":"2023-12-14T11:40:34.775Z","validFromDate":"2023-12-14T22:15:54.588Z","validThruDate":"2023-12-14T02:17:30.788Z","description":"psst abacus","offerValue":28924.29,"remarks":"generously dampen"},
    }).then(({ body }) => {
      quote = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/quote-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/quote-items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/quote-items/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/quotes', {
      statusCode: 200,
      body: [quote],
    });

    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/product-features', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/uoms', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/statuses', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/cust-requests', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/cust-request-items', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (quoteItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/quote-items/${quoteItem.id}`,
      }).then(() => {
        quoteItem = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (quote) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/quotes/${quote.id}`,
      }).then(() => {
        quote = undefined;
      });
    }
  });
   */

  it('QuoteItems menu should load QuoteItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('quote-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('QuoteItem').should('exist');
    cy.location('pathname').should('eq', quoteItemPageUrl);
  });

  describe('QuoteItem page', () => {
    it('should have translated page title', () => {
      cy.visit(quoteItemPageUrl);
      cy.getEntityHeading('QuoteItem').should('not.contain', 'snpApp.quoteItem.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(quoteItemPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create QuoteItem page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${quoteItemPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('QuoteItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quoteItemPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/quote-items',
          body: {
            ...quoteItemSample,
            quote: quote,
          },
        }).then(({ body }) => {
          quoteItem = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/quote-items+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/quote-items?page=0&size=20>; rel="last",<http://localhost/api/quote-items?page=0&size=20>; rel="first"',
              },
              body: [quoteItem],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(quoteItemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(quoteItemPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details QuoteItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('quoteItem');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quoteItemPageUrl);
      });

      it('edit button click should load edit QuoteItem page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('QuoteItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quoteItemPageUrl);
      });

      it('edit button click should load edit QuoteItem page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('QuoteItem');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quoteItemPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of QuoteItem', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('quoteItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quoteItemPageUrl);

        quoteItem = undefined;
      });
    });
  });

  describe('new QuoteItem page', () => {
    beforeEach(() => {
      cy.visit(quoteItemPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('QuoteItem');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of QuoteItem', () => {
      cy.get(`[data-cy="sequenceNo"]`).type('25533');
      cy.get(`[data-cy="sequenceNo"]`).should('have.value', '25533');

      cy.get(`[data-cy="quantity"]`).type('4165.74');
      cy.get(`[data-cy="quantity"]`).should('have.value', '4165.74');

      cy.get(`[data-cy="selectedAmount"]`).type('3728.85');
      cy.get(`[data-cy="selectedAmount"]`).should('have.value', '3728.85');

      cy.get(`[data-cy="quoteUnitPrice"]`).type('28988.1');
      cy.get(`[data-cy="quoteUnitPrice"]`).should('have.value', '28988.1');

      cy.get(`[data-cy="totalAmount"]`).type('28818.47');
      cy.get(`[data-cy="totalAmount"]`).should('have.value', '28818.47');

      cy.get(`[data-cy="estimatedDeliveryDate"]`).type('2023-12-14T16:53');
      cy.get(`[data-cy="estimatedDeliveryDate"]`).blur();
      cy.get(`[data-cy="estimatedDeliveryDate"]`).should('have.value', '2023-12-14T16:53');

      cy.get(`[data-cy="leadTimeDays"]`).type('12738');
      cy.get(`[data-cy="leadTimeDays"]`).should('have.value', '12738');

      cy.get(`[data-cy="comments"]`).type('powerless gruesome ugh');
      cy.get(`[data-cy="comments"]`).should('have.value', 'powerless gruesome ugh');

      cy.get(`[data-cy="quote"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        quoteItem = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', quoteItemPageUrl);
    });
  });
});
