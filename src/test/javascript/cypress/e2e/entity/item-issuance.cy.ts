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

describe('ItemIssuance e2e test', () => {
  const itemIssuancePageUrl = '/item-issuance';
  let username: string;
  let password: string;
  // const itemIssuanceSample = {"quantity":3866.44};

  let itemIssuance;
  // let inventoryItem;

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
      url: '/api/inventory-items',
      body: {"receivedDate":"2023-12-14T10:42:47.433Z","manufacturedDate":"2023-12-14T01:03:28.775Z","expiryDate":"2023-12-14T14:01:53.871Z","retestDate":"2023-12-14T13:02:01.754Z","containerId":"and and","batchNo":"in","mfgBatchNo":"flashy bootleg slump","lotNoStr":"know","binNumber":"wildly for hm","comments":"whenever","quantityOnHandTotal":2015.86,"availableToPromiseTotal":19718.26,"accountingQuantityTotal":13102.01,"oldQuantityOnHand":13710.67,"oldAvailableToPromise":30893.93,"serialNumber":"like zowie demobilise","softIdentifier":"dispose successfully drat","activationNumber":"however grave because","activationValidThru":"2023-12-13T22:54:57.207Z","unitCost":17274.14},
    }).then(({ body }) => {
      inventoryItem = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/item-issuances+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/item-issuances').as('postEntityRequest');
    cy.intercept('DELETE', '/api/item-issuances/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/inventory-items', {
      statusCode: 200,
      body: [inventoryItem],
    });

    cy.intercept('GET', '/api/orders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/order-items', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/shipments', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/shipment-items', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/parties', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (itemIssuance) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/item-issuances/${itemIssuance.id}`,
      }).then(() => {
        itemIssuance = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (inventoryItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/inventory-items/${inventoryItem.id}`,
      }).then(() => {
        inventoryItem = undefined;
      });
    }
  });
   */

  it('ItemIssuances menu should load ItemIssuances page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-issuance');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ItemIssuance').should('exist');
    cy.location('pathname').should('eq', itemIssuancePageUrl);
  });

  describe('ItemIssuance page', () => {
    it('should have translated page title', () => {
      cy.visit(itemIssuancePageUrl);
      cy.getEntityHeading('ItemIssuance').should('not.contain', 'snpApp.itemIssuance.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(itemIssuancePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ItemIssuance page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${itemIssuancePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ItemIssuance');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', itemIssuancePageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/item-issuances',
          body: {
            ...itemIssuanceSample,
            inventoryItem: inventoryItem,
          },
        }).then(({ body }) => {
          itemIssuance = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/item-issuances+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/item-issuances?page=0&size=20>; rel="last",<http://localhost/api/item-issuances?page=0&size=20>; rel="first"',
              },
              body: [itemIssuance],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(itemIssuancePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(itemIssuancePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details ItemIssuance page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('itemIssuance');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', itemIssuancePageUrl);
      });

      it('edit button click should load edit ItemIssuance page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ItemIssuance');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', itemIssuancePageUrl);
      });

      it('edit button click should load edit ItemIssuance page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ItemIssuance');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', itemIssuancePageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of ItemIssuance', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('itemIssuance').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', itemIssuancePageUrl);

        itemIssuance = undefined;
      });
    });
  });

  describe('new ItemIssuance page', () => {
    beforeEach(() => {
      cy.visit(itemIssuancePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ItemIssuance');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of ItemIssuance', () => {
      cy.get(`[data-cy="message"]`).type('although consequently');
      cy.get(`[data-cy="message"]`).should('have.value', 'although consequently');

      cy.get(`[data-cy="issuedDate"]`).type('2023-12-14T05:16');
      cy.get(`[data-cy="issuedDate"]`).blur();
      cy.get(`[data-cy="issuedDate"]`).should('have.value', '2023-12-14T05:16');

      cy.get(`[data-cy="issuedBy"]`).type('penalise');
      cy.get(`[data-cy="issuedBy"]`).should('have.value', 'penalise');

      cy.get(`[data-cy="quantity"]`).type('9933.1');
      cy.get(`[data-cy="quantity"]`).should('have.value', '9933.1');

      cy.get(`[data-cy="cancelQuantity"]`).type('19833.85');
      cy.get(`[data-cy="cancelQuantity"]`).should('have.value', '19833.85');

      cy.get(`[data-cy="inventoryItem"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        itemIssuance = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', itemIssuancePageUrl);
    });
  });
});
