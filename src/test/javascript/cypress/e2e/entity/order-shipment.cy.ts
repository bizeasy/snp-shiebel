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

describe('OrderShipment e2e test', () => {
  const orderShipmentPageUrl = '/order-shipment';
  let username: string;
  let password: string;
  // const orderShipmentSample = {"quantity":11275.09};

  let orderShipment;
  // let order;
  // let orderItem;
  // let shipment;

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
      body: {"name":"outstanding","orderRefNo":"notwithstanding hutch","externalId":"approach mostly amongst","orderDate":"2023-12-14T03:47:39.078Z","entryDate":"2023-12-14T03:54:59.814Z","deliveryDate":"2023-12-14","priority":1,"isRushOrder":false,"needsInventoryIssuance":false,"remainingSubTotal":30071.69,"grandTotal":9016.02,"projectName":"farm far-flung","version":15923,"versionRef":"geez between per","description":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","remarks":"freckle whimsical circa","paymentTerms":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","deliveryTerms":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","info":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","attr1":"yum","attr2":"until","attr3":"quiet pliers"},
    }).then(({ body }) => {
      order = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/order-items',
      body: {"sequenceNo":226,"quantity":30657.77,"cancelQuantity":23859.41,"selectedAmount":28372.72,"unitPrice":15237.7,"unitListPrice":8128.39,"isModifiedPrice":true,"estimatedShipDate":"2023-12-14T06:40:25.002Z","estimatedDeliveryDate":"2023-12-14T04:12:19.385Z","shipBeforeDate":"2023-12-14T19:55:10.308Z","shipAfterDate":"2023-12-14T08:03:37.454Z","entryDate":"2023-12-14T10:48:29.969Z","attr1":"given ceramics","attr2":"gorgeous","attr3":"excepting narrow across"},
    }).then(({ body }) => {
      orderItem = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/shipments',
      body: {"estimatedShipDate":"2023-12-14T00:13:35.422Z","estimatedReadyDate":"2023-12-14T18:53:06.579Z","estimatedArrivalDate":"2023-12-14T19:20:03.778Z","estimatedShipCost":22042.83},
    }).then(({ body }) => {
      shipment = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/order-shipments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/order-shipments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/order-shipments/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/orders', {
      statusCode: 200,
      body: [order],
    });

    cy.intercept('GET', '/api/order-items', {
      statusCode: 200,
      body: [orderItem],
    });

    cy.intercept('GET', '/api/shipments', {
      statusCode: 200,
      body: [shipment],
    });

    cy.intercept('GET', '/api/shipment-items', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (orderShipment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/order-shipments/${orderShipment.id}`,
      }).then(() => {
        orderShipment = undefined;
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
    if (orderItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/order-items/${orderItem.id}`,
      }).then(() => {
        orderItem = undefined;
      });
    }
    if (shipment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/shipments/${shipment.id}`,
      }).then(() => {
        shipment = undefined;
      });
    }
  });
   */

  it('OrderShipments menu should load OrderShipments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('order-shipment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrderShipment').should('exist');
    cy.location('pathname').should('eq', orderShipmentPageUrl);
  });

  describe('OrderShipment page', () => {
    it('should have translated page title', () => {
      cy.visit(orderShipmentPageUrl);
      cy.getEntityHeading('OrderShipment').should('not.contain', 'snpApp.orderShipment.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(orderShipmentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrderShipment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${orderShipmentPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('OrderShipment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderShipmentPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/order-shipments',
          body: {
            ...orderShipmentSample,
            order: order,
            orderItem: orderItem,
            shipment: shipment,
          },
        }).then(({ body }) => {
          orderShipment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/order-shipments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/order-shipments?page=0&size=20>; rel="last",<http://localhost/api/order-shipments?page=0&size=20>; rel="first"',
              },
              body: [orderShipment],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(orderShipmentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(orderShipmentPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details OrderShipment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('orderShipment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderShipmentPageUrl);
      });

      it('edit button click should load edit OrderShipment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderShipment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderShipmentPageUrl);
      });

      it('edit button click should load edit OrderShipment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderShipment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderShipmentPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of OrderShipment', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('orderShipment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderShipmentPageUrl);

        orderShipment = undefined;
      });
    });
  });

  describe('new OrderShipment page', () => {
    beforeEach(() => {
      cy.visit(orderShipmentPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OrderShipment');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of OrderShipment', () => {
      cy.get(`[data-cy="quantity"]`).type('22518.26');
      cy.get(`[data-cy="quantity"]`).should('have.value', '22518.26');

      cy.get(`[data-cy="order"]`).select(1);
      cy.get(`[data-cy="orderItem"]`).select(1);
      cy.get(`[data-cy="shipment"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        orderShipment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', orderShipmentPageUrl);
    });
  });
});
