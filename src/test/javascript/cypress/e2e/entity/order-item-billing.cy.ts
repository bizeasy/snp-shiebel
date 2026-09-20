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

describe('OrderItemBilling e2e test', () => {
  const orderItemBillingPageUrl = '/order-item-billing';
  let username: string;
  let password: string;
  // const orderItemBillingSample = {"quantity":3874.25};

  let orderItemBilling;
  // let orderItem;
  // let invoiceItem;

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
      url: '/api/order-items',
      body: {"sequenceNo":25168,"quantity":17460.92,"cancelQuantity":27579.77,"selectedAmount":12919.22,"unitPrice":459.97,"unitListPrice":1317.26,"isModifiedPrice":false,"estimatedShipDate":"2023-12-14T18:52:28.670Z","estimatedDeliveryDate":"2023-12-14T06:09:37.978Z","shipBeforeDate":"2023-12-14T05:45:05.962Z","shipAfterDate":"2023-12-14T03:20:51.656Z","entryDate":"2023-12-14T11:05:00.604Z","attr1":"napkin scratchy hm","attr2":"bar minus uselessly","attr3":"regularly"},
    }).then(({ body }) => {
      orderItem = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/invoice-items',
      body: {"sequenceNo":5885,"quantity":24698.5,"amount":7659.95,"cancelQuantity":2304.2,"selectedAmount":9083.93,"unitPrice":14929.27,"unitListPrice":19704.57,"isModifiedPrice":true,"estimatedShipDate":"2023-12-14T16:33:52.226Z","estimatedDeliveryDate":"2023-12-14T17:24:33.502Z","shipBeforeDate":"2023-12-14T05:02:34.024Z","shipAfterDate":"2023-12-14T03:37:00.365Z","entryDate":"2023-12-14T06:10:37.552Z","attr1":"considering limply","attr2":"commercial unconscious","attr3":"tightly"},
    }).then(({ body }) => {
      invoiceItem = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/order-item-billings+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/order-item-billings').as('postEntityRequest');
    cy.intercept('DELETE', '/api/order-item-billings/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/order-items', {
      statusCode: 200,
      body: [orderItem],
    });

    cy.intercept('GET', '/api/invoice-items', {
      statusCode: 200,
      body: [invoiceItem],
    });

    cy.intercept('GET', '/api/shipment-receipts', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/item-issuances', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (orderItemBilling) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/order-item-billings/${orderItemBilling.id}`,
      }).then(() => {
        orderItemBilling = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (orderItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/order-items/${orderItem.id}`,
      }).then(() => {
        orderItem = undefined;
      });
    }
    if (invoiceItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/invoice-items/${invoiceItem.id}`,
      }).then(() => {
        invoiceItem = undefined;
      });
    }
  });
   */

  it('OrderItemBillings menu should load OrderItemBillings page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('order-item-billing');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrderItemBilling').should('exist');
    cy.location('pathname').should('eq', orderItemBillingPageUrl);
  });

  describe('OrderItemBilling page', () => {
    it('should have translated page title', () => {
      cy.visit(orderItemBillingPageUrl);
      cy.getEntityHeading('OrderItemBilling').should('not.contain', 'snpApp.orderItemBilling.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(orderItemBillingPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrderItemBilling page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${orderItemBillingPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('OrderItemBilling');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderItemBillingPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/order-item-billings',
          body: {
            ...orderItemBillingSample,
            orderItem: orderItem,
            invoiceItem: invoiceItem,
          },
        }).then(({ body }) => {
          orderItemBilling = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/order-item-billings+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/order-item-billings?page=0&size=20>; rel="last",<http://localhost/api/order-item-billings?page=0&size=20>; rel="first"',
              },
              body: [orderItemBilling],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(orderItemBillingPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(orderItemBillingPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details OrderItemBilling page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('orderItemBilling');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderItemBillingPageUrl);
      });

      it('edit button click should load edit OrderItemBilling page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderItemBilling');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderItemBillingPageUrl);
      });

      it('edit button click should load edit OrderItemBilling page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderItemBilling');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderItemBillingPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of OrderItemBilling', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('orderItemBilling').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderItemBillingPageUrl);

        orderItemBilling = undefined;
      });
    });
  });

  describe('new OrderItemBilling page', () => {
    beforeEach(() => {
      cy.visit(orderItemBillingPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OrderItemBilling');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of OrderItemBilling', () => {
      cy.get(`[data-cy="quantity"]`).type('15044.22');
      cy.get(`[data-cy="quantity"]`).should('have.value', '15044.22');

      cy.get(`[data-cy="amount"]`).type('24634.56');
      cy.get(`[data-cy="amount"]`).should('have.value', '24634.56');

      cy.get(`[data-cy="orderItem"]`).select(1);
      cy.get(`[data-cy="invoiceItem"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        orderItemBilling = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', orderItemBillingPageUrl);
    });
  });
});
