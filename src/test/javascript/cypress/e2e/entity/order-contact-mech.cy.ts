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

describe('OrderContactMech e2e test', () => {
  const orderContactMechPageUrl = '/order-contact-mech';
  let username: string;
  let password: string;
  // const orderContactMechSample = {"fromDate":"2023-12-14T15:33:49.021Z"};

  let orderContactMech;
  // let order;
  // let contactMech;

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
      body: {"name":"footrest","orderRefNo":"guidance sociable","externalId":"incidentally","orderDate":"2023-12-14T21:13:35.024Z","entryDate":"2023-12-14T10:45:41.848Z","deliveryDate":"2023-12-14","priority":8,"isRushOrder":true,"needsInventoryIssuance":true,"remainingSubTotal":26660.19,"grandTotal":19217.56,"projectName":"pace","version":25652,"versionRef":"to minus","description":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","remarks":"elementary beautifully","paymentTerms":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","deliveryTerms":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","info":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","attr1":"by husband tenderly","attr2":"failing upliftingly","attr3":"gee athletic"},
    }).then(({ body }) => {
      order = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/contact-meches',
      body: {"infoString":"decisive schematise contrail"},
    }).then(({ body }) => {
      contactMech = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/order-contact-meches+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/order-contact-meches').as('postEntityRequest');
    cy.intercept('DELETE', '/api/order-contact-meches/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/orders', {
      statusCode: 200,
      body: [order],
    });

    cy.intercept('GET', '/api/contact-meches', {
      statusCode: 200,
      body: [contactMech],
    });

    cy.intercept('GET', '/api/contact-mech-purposes', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (orderContactMech) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/order-contact-meches/${orderContactMech.id}`,
      }).then(() => {
        orderContactMech = undefined;
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
    if (contactMech) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/contact-meches/${contactMech.id}`,
      }).then(() => {
        contactMech = undefined;
      });
    }
  });
   */

  it('OrderContactMeches menu should load OrderContactMeches page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('order-contact-mech');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrderContactMech').should('exist');
    cy.location('pathname').should('eq', orderContactMechPageUrl);
  });

  describe('OrderContactMech page', () => {
    it('should have translated page title', () => {
      cy.visit(orderContactMechPageUrl);
      cy.getEntityHeading('OrderContactMech').should('not.contain', 'snpApp.orderContactMech.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(orderContactMechPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrderContactMech page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${orderContactMechPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('OrderContactMech');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderContactMechPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/order-contact-meches',
          body: {
            ...orderContactMechSample,
            order: order,
            contactMech: contactMech,
          },
        }).then(({ body }) => {
          orderContactMech = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/order-contact-meches+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/order-contact-meches?page=0&size=20>; rel="last",<http://localhost/api/order-contact-meches?page=0&size=20>; rel="first"',
              },
              body: [orderContactMech],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(orderContactMechPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(orderContactMechPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details OrderContactMech page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('orderContactMech');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderContactMechPageUrl);
      });

      it('edit button click should load edit OrderContactMech page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderContactMech');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderContactMechPageUrl);
      });

      it('edit button click should load edit OrderContactMech page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderContactMech');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderContactMechPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of OrderContactMech', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('orderContactMech').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderContactMechPageUrl);

        orderContactMech = undefined;
      });
    });
  });

  describe('new OrderContactMech page', () => {
    beforeEach(() => {
      cy.visit(orderContactMechPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OrderContactMech');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of OrderContactMech', () => {
      cy.get(`[data-cy="fromDate"]`).type('2023-12-14T01:10');
      cy.get(`[data-cy="fromDate"]`).blur();
      cy.get(`[data-cy="fromDate"]`).should('have.value', '2023-12-14T01:10');

      cy.get(`[data-cy="thruDate"]`).type('2023-12-13T23:18');
      cy.get(`[data-cy="thruDate"]`).blur();
      cy.get(`[data-cy="thruDate"]`).should('have.value', '2023-12-13T23:18');

      cy.get(`[data-cy="order"]`).select(1);
      cy.get(`[data-cy="contactMech"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        orderContactMech = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', orderContactMechPageUrl);
    });
  });
});
