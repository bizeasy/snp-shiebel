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

describe('OrderItem e2e test', () => {
  const orderItemPageUrl = '/order-item';
  let username: string;
  let password: string;
  // const orderItemSample = {"quantity":8133.56};

  let orderItem;
  // let order;

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
      url: '/api/orders',
      body: {"name":"alert noisily","orderRefNo":"heavy","externalId":"frightfully whenever","orderDate":"2023-12-14T16:01:47.304Z","entryDate":"2023-12-14T13:37:35.375Z","deliveryDate":"2023-12-14","priority":2,"isRushOrder":false,"needsInventoryIssuance":true,"remainingSubTotal":26704.99,"grandTotal":32321.51,"projectName":"wash","version":16800,"versionRef":"vengeful put","description":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","remarks":"through blah","paymentTerms":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","deliveryTerms":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","info":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","attr1":"descendant yuck","attr2":"priesthood meanwhile deceivingly","attr3":"afterwards"},
    }).then(({ body }) => {
      order = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/order-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/order-items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/order-items/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/orders', {
      statusCode: 200,
      body: [order],
    });

    cy.intercept('GET', '/api/order-item-types', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/uoms', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/supplier-products', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/parties', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/product-features', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/product-facilities', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/inventory-items', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/shopping-list-items', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/quote-items', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/statuses', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/tax-authority-rate-types', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (orderItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/order-items/${orderItem.id}`,
      }).then(() => {
        orderItem = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (order) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/orders/${order.id}`,
      }).then(() => {
        order = undefined;
      });
    }
  });
   */

  it('OrderItems menu should load OrderItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('order-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrderItem').should('exist');
    cy.location('pathname').should('eq', orderItemPageUrl);
  });

  describe('OrderItem page', () => {
    it('should have translated page title', () => {
      cy.visit(orderItemPageUrl);
      cy.getEntityHeading('OrderItem').should('not.contain', 'snpApp.orderItem.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(orderItemPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrderItem page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${orderItemPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('OrderItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderItemPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/order-items',
          body: {
            ...orderItemSample,
            order: order,
          },
        }).then(({ body }) => {
          orderItem = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/order-items+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/order-items?page=0&size=20>; rel="last",<http://localhost/api/order-items?page=0&size=20>; rel="first"',
              },
              body: [orderItem],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(orderItemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(orderItemPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details OrderItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('orderItem');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderItemPageUrl);
      });

      it('edit button click should load edit OrderItem page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderItemPageUrl);
      });

      it('edit button click should load edit OrderItem page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderItem');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderItemPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of OrderItem', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('orderItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderItemPageUrl);

        orderItem = undefined;
      });
    });
  });

  describe('new OrderItem page', () => {
    beforeEach(() => {
      cy.visit(orderItemPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OrderItem');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of OrderItem', () => {
      cy.get(`[data-cy="sequenceNo"]`).type('10286');
      cy.get(`[data-cy="sequenceNo"]`).should('have.value', '10286');

      cy.get(`[data-cy="quantity"]`).type('14087.64');
      cy.get(`[data-cy="quantity"]`).should('have.value', '14087.64');

      cy.get(`[data-cy="cancelQuantity"]`).type('4305.27');
      cy.get(`[data-cy="cancelQuantity"]`).should('have.value', '4305.27');

      cy.get(`[data-cy="selectedAmount"]`).type('11729.18');
      cy.get(`[data-cy="selectedAmount"]`).should('have.value', '11729.18');

      cy.get(`[data-cy="unitPrice"]`).type('23072.99');
      cy.get(`[data-cy="unitPrice"]`).should('have.value', '23072.99');

      cy.get(`[data-cy="unitListPrice"]`).type('10141.83');
      cy.get(`[data-cy="unitListPrice"]`).should('have.value', '10141.83');

      cy.get(`[data-cy="isModifiedPrice"]`).should('not.be.checked');
      cy.get(`[data-cy="isModifiedPrice"]`).click();
      cy.get(`[data-cy="isModifiedPrice"]`).should('be.checked');

      cy.get(`[data-cy="estimatedShipDate"]`).type('2023-12-14T06:01');
      cy.get(`[data-cy="estimatedShipDate"]`).blur();
      cy.get(`[data-cy="estimatedShipDate"]`).should('have.value', '2023-12-14T06:01');

      cy.get(`[data-cy="estimatedDeliveryDate"]`).type('2023-12-14T03:19');
      cy.get(`[data-cy="estimatedDeliveryDate"]`).blur();
      cy.get(`[data-cy="estimatedDeliveryDate"]`).should('have.value', '2023-12-14T03:19');

      cy.get(`[data-cy="shipBeforeDate"]`).type('2023-12-14T21:38');
      cy.get(`[data-cy="shipBeforeDate"]`).blur();
      cy.get(`[data-cy="shipBeforeDate"]`).should('have.value', '2023-12-14T21:38');

      cy.get(`[data-cy="shipAfterDate"]`).type('2023-12-14T19:15');
      cy.get(`[data-cy="shipAfterDate"]`).blur();
      cy.get(`[data-cy="shipAfterDate"]`).should('have.value', '2023-12-14T19:15');

      cy.get(`[data-cy="entryDate"]`).type('2023-12-13T23:51');
      cy.get(`[data-cy="entryDate"]`).blur();
      cy.get(`[data-cy="entryDate"]`).should('have.value', '2023-12-13T23:51');

      cy.get(`[data-cy="attr1"]`).type('amid overheard');
      cy.get(`[data-cy="attr1"]`).should('have.value', 'amid overheard');

      cy.get(`[data-cy="attr2"]`).type('if');
      cy.get(`[data-cy="attr2"]`).should('have.value', 'if');

      cy.get(`[data-cy="attr3"]`).type('nice eek');
      cy.get(`[data-cy="attr3"]`).should('have.value', 'nice eek');

      cy.get(`[data-cy="order"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        orderItem = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', orderItemPageUrl);
    });
  });
});
