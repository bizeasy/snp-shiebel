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

describe('ReturnItemBilling e2e test', () => {
  const returnItemBillingPageUrl = '/return-item-billing';
  let username: string;
  let password: string;
  // const returnItemBillingSample = {"quantity":9217.04};

  let returnItemBilling;
  // let returnItem;

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
      url: '/api/return-items',
      body: {"sequenceNo":6496,"description":"as correctly","returnQuantity":11737.5,"receivedQuantity":14487.77,"returnPrice":16955.69,"returnItemPurpose":"mmm adjudge instead"},
    }).then(({ body }) => {
      returnItem = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/return-item-billings+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/return-item-billings').as('postEntityRequest');
    cy.intercept('DELETE', '/api/return-item-billings/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/return-items', {
      statusCode: 200,
      body: [returnItem],
    });

    cy.intercept('GET', '/api/order-items', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/invoice-items', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/shipment-receipts', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (returnItemBilling) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/return-item-billings/${returnItemBilling.id}`,
      }).then(() => {
        returnItemBilling = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (returnItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/return-items/${returnItem.id}`,
      }).then(() => {
        returnItem = undefined;
      });
    }
  });
   */

  it('ReturnItemBillings menu should load ReturnItemBillings page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('return-item-billing');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ReturnItemBilling').should('exist');
    cy.location('pathname').should('eq', returnItemBillingPageUrl);
  });

  describe('ReturnItemBilling page', () => {
    it('should have translated page title', () => {
      cy.visit(returnItemBillingPageUrl);
      cy.getEntityHeading('ReturnItemBilling').should('not.contain', 'snpApp.returnItemBilling.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(returnItemBillingPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ReturnItemBilling page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${returnItemBillingPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ReturnItemBilling');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', returnItemBillingPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/return-item-billings',
          body: {
            ...returnItemBillingSample,
            returnItem: returnItem,
          },
        }).then(({ body }) => {
          returnItemBilling = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/return-item-billings+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/return-item-billings?page=0&size=20>; rel="last",<http://localhost/api/return-item-billings?page=0&size=20>; rel="first"',
              },
              body: [returnItemBilling],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(returnItemBillingPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(returnItemBillingPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details ReturnItemBilling page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('returnItemBilling');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', returnItemBillingPageUrl);
      });

      it('edit button click should load edit ReturnItemBilling page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReturnItemBilling');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', returnItemBillingPageUrl);
      });

      it('edit button click should load edit ReturnItemBilling page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReturnItemBilling');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', returnItemBillingPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of ReturnItemBilling', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('returnItemBilling').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', returnItemBillingPageUrl);

        returnItemBilling = undefined;
      });
    });
  });

  describe('new ReturnItemBilling page', () => {
    beforeEach(() => {
      cy.visit(returnItemBillingPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ReturnItemBilling');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of ReturnItemBilling', () => {
      cy.get(`[data-cy="quantity"]`).type('14154.6');
      cy.get(`[data-cy="quantity"]`).should('have.value', '14154.6');

      cy.get(`[data-cy="amount"]`).type('29569.62');
      cy.get(`[data-cy="amount"]`).should('have.value', '29569.62');

      cy.get(`[data-cy="returnItem"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        returnItemBilling = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', returnItemBillingPageUrl);
    });
  });
});
