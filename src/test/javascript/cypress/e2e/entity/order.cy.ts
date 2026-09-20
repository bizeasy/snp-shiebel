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

describe('Order e2e test', () => {
  const orderPageUrl = '/order';
  let username: string;
  let password: string;
  const orderSample = { orderDate: '2023-12-14T11:23:44.454Z' };

  let order;
  let orderType;
  let party;

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
      url: '/api/order-types',
      body: { name: 'ew printer past', description: 'crystallize for apropos' },
    }).then(({ body }) => {
      orderType = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/parties',
      body: {
        isOrganisation: false,
        organisationName: 'upside-down',
        organisationShortName: 'apropos',
        salutation: 'off among aboard',
        firstName: 'Loren',
        middleName: 'upon',
        lastName: "O'Connell-Streich",
        gender: 'OTHER',
        birthDate: '2023-12-13',
        primaryPhone: 'between viciously ha',
        primaryEmail: 'hoseX',
        isTemporaryPassword: false,
        logoImageUrl: 'vulgarise yet while',
        profileImageUrl: 'sushi',
        notes: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        postalAddress: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      party = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/orders+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/orders').as('postEntityRequest');
    cy.intercept('DELETE', '/api/orders/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/order-types', {
      statusCode: 200,
      body: [orderType],
    });

    cy.intercept('GET', '/api/sales-channels', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/parties', {
      statusCode: 200,
      body: [party],
    });

    cy.intercept('GET', '/api/statuses', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/uoms', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/uom-conversions', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/quotes', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/facilities', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/product-stores', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/departments', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/custom-time-periods', {
      statusCode: 200,
      body: [],
    });
  });

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

  afterEach(() => {
    if (orderType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/order-types/${orderType.id}`,
      }).then(() => {
        orderType = undefined;
      });
    }
    if (party) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/parties/${party.id}`,
      }).then(() => {
        party = undefined;
      });
    }
  });

  it('Orders menu should load Orders page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('order');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Order').should('exist');
    cy.location('pathname').should('eq', orderPageUrl);
  });

  describe('Order page', () => {
    it('should have translated page title', () => {
      cy.visit(orderPageUrl);
      cy.getEntityHeading('Order').should('not.contain', 'snpApp.order.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(orderPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Order page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${orderPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Order');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/orders',
          body: {
            ...orderSample,
            orderType,
            party,
          },
        }).then(({ body }) => {
          order = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/orders+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/orders?page=0&size=20>; rel="last",<http://localhost/api/orders?page=0&size=20>; rel="first"',
              },
              body: [order],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(orderPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Order page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('order');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderPageUrl);
      });

      it('edit button click should load edit Order page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Order');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderPageUrl);
      });

      it('edit button click should load edit Order page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Order');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderPageUrl);
      });

      it('last delete button click should delete instance of Order', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('order').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderPageUrl);

        order = undefined;
      });
    });
  });

  describe('new Order page', () => {
    beforeEach(() => {
      cy.visit(orderPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Order');
    });

    it('should create an instance of Order', () => {
      cy.get(`[data-cy="name"]`).type('impressive');
      cy.get(`[data-cy="name"]`).should('have.value', 'impressive');

      cy.get(`[data-cy="orderRefNo"]`).type('intently ultimately curly');
      cy.get(`[data-cy="orderRefNo"]`).should('have.value', 'intently ultimately curly');

      cy.get(`[data-cy="externalId"]`).type('triumphantly for unfortun');
      cy.get(`[data-cy="externalId"]`).should('have.value', 'triumphantly for unfortun');

      cy.get(`[data-cy="orderDate"]`).type('2023-12-14T06:17');
      cy.get(`[data-cy="orderDate"]`).blur();
      cy.get(`[data-cy="orderDate"]`).should('have.value', '2023-12-14T06:17');

      cy.get(`[data-cy="entryDate"]`).type('2023-12-14T11:43');
      cy.get(`[data-cy="entryDate"]`).blur();
      cy.get(`[data-cy="entryDate"]`).should('have.value', '2023-12-14T11:43');

      cy.get(`[data-cy="deliveryDate"]`).type('2023-12-14');
      cy.get(`[data-cy="deliveryDate"]`).blur();
      cy.get(`[data-cy="deliveryDate"]`).should('have.value', '2023-12-14');

      cy.get(`[data-cy="priority"]`).type('9');
      cy.get(`[data-cy="priority"]`).should('have.value', '9');

      cy.get(`[data-cy="isRushOrder"]`).should('not.be.checked');
      cy.get(`[data-cy="isRushOrder"]`).click();
      cy.get(`[data-cy="isRushOrder"]`).should('be.checked');

      cy.get(`[data-cy="needsInventoryIssuance"]`).should('not.be.checked');
      cy.get(`[data-cy="needsInventoryIssuance"]`).click();
      cy.get(`[data-cy="needsInventoryIssuance"]`).should('be.checked');

      cy.get(`[data-cy="remainingSubTotal"]`).type('18748.36');
      cy.get(`[data-cy="remainingSubTotal"]`).should('have.value', '18748.36');

      cy.get(`[data-cy="grandTotal"]`).type('22748.33');
      cy.get(`[data-cy="grandTotal"]`).should('have.value', '22748.33');

      cy.get(`[data-cy="projectName"]`).type('misguided');
      cy.get(`[data-cy="projectName"]`).should('have.value', 'misguided');

      cy.get(`[data-cy="version"]`).type('28513');
      cy.get(`[data-cy="version"]`).should('have.value', '28513');

      cy.get(`[data-cy="versionRef"]`).type('politely');
      cy.get(`[data-cy="versionRef"]`).should('have.value', 'politely');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="remarks"]`).type('vanadyl international');
      cy.get(`[data-cy="remarks"]`).should('have.value', 'vanadyl international');

      cy.get(`[data-cy="paymentTerms"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="paymentTerms"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="deliveryTerms"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="deliveryTerms"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="info"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="info"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="attr1"]`).type('stuff yesterday');
      cy.get(`[data-cy="attr1"]`).should('have.value', 'stuff yesterday');

      cy.get(`[data-cy="attr2"]`).type('grubby march impressive');
      cy.get(`[data-cy="attr2"]`).should('have.value', 'grubby march impressive');

      cy.get(`[data-cy="attr3"]`).type('celebrated trim');
      cy.get(`[data-cy="attr3"]`).should('have.value', 'celebrated trim');

      cy.get(`[data-cy="orderType"]`).select(1);
      cy.get(`[data-cy="party"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        order = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', orderPageUrl);
    });
  });
});
