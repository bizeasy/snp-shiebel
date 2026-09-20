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

describe('OrderStatus e2e test', () => {
  const orderStatusPageUrl = '/order-status';
  let username: string;
  let password: string;
  // const orderStatusSample = {"statusDateTime":"2023-12-14T01:30:52.160Z"};

  let orderStatus;
  // let order;
  // let status;

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
      body: {"name":"what after","orderRefNo":"baa briskly absentmindedly","externalId":"towards exasperation inst","orderDate":"2023-12-14T08:10:33.263Z","entryDate":"2023-12-14T10:08:40.295Z","deliveryDate":"2023-12-14","priority":9,"isRushOrder":true,"needsInventoryIssuance":false,"remainingSubTotal":6047.81,"grandTotal":16782.47,"projectName":"gadzooks descriptive coast","version":24649,"versionRef":"interestingly notwithstanding regarding","description":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","remarks":"between elementary","paymentTerms":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","deliveryTerms":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","info":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","attr1":"via","attr2":"shocked minus across","attr3":"plus"},
    }).then(({ body }) => {
      order = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/statuses',
      body: {"name":"where of","sequenceNo":27799,"description":"fond regal inwardly"},
    }).then(({ body }) => {
      status = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/order-statuses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/order-statuses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/order-statuses/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/orders', {
      statusCode: 200,
      body: [order],
    });

    cy.intercept('GET', '/api/statuses', {
      statusCode: 200,
      body: [status],
    });

  });
   */

  afterEach(() => {
    if (orderStatus) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/order-statuses/${orderStatus.id}`,
      }).then(() => {
        orderStatus = undefined;
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
    if (status) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/statuses/${status.id}`,
      }).then(() => {
        status = undefined;
      });
    }
  });
   */

  it('OrderStatuses menu should load OrderStatuses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('order-status');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrderStatus').should('exist');
    cy.location('pathname').should('eq', orderStatusPageUrl);
  });

  describe('OrderStatus page', () => {
    it('should have translated page title', () => {
      cy.visit(orderStatusPageUrl);
      cy.getEntityHeading('OrderStatus').should('not.contain', 'snpApp.orderStatus.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(orderStatusPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrderStatus page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${orderStatusPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('OrderStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderStatusPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/order-statuses',
          body: {
            ...orderStatusSample,
            order: order,
            status: status,
          },
        }).then(({ body }) => {
          orderStatus = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/order-statuses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/order-statuses?page=0&size=20>; rel="last",<http://localhost/api/order-statuses?page=0&size=20>; rel="first"',
              },
              body: [orderStatus],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(orderStatusPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(orderStatusPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details OrderStatus page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('orderStatus');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderStatusPageUrl);
      });

      it('edit button click should load edit OrderStatus page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderStatusPageUrl);
      });

      it('edit button click should load edit OrderStatus page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderStatus');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderStatusPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of OrderStatus', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('orderStatus').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderStatusPageUrl);

        orderStatus = undefined;
      });
    });
  });

  describe('new OrderStatus page', () => {
    beforeEach(() => {
      cy.visit(orderStatusPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OrderStatus');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of OrderStatus', () => {
      cy.get(`[data-cy="statusDateTime"]`).type('2023-12-14T04:50');
      cy.get(`[data-cy="statusDateTime"]`).blur();
      cy.get(`[data-cy="statusDateTime"]`).should('have.value', '2023-12-14T04:50');

      cy.get(`[data-cy="sequenceNo"]`).type('18468');
      cy.get(`[data-cy="sequenceNo"]`).should('have.value', '18468');

      cy.get(`[data-cy="order"]`).select(1);
      cy.get(`[data-cy="status"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        orderStatus = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', orderStatusPageUrl);
    });
  });
});
