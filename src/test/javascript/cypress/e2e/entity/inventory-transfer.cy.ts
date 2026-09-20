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

describe('InventoryTransfer e2e test', () => {
  const inventoryTransferPageUrl = '/inventory-transfer';
  let username: string;
  let password: string;
  // const inventoryTransferSample = {};

  let inventoryTransfer;
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
      body: {"receivedDate":"2023-12-13T23:59:10.193Z","manufacturedDate":"2023-12-14T01:11:55.245Z","expiryDate":"2023-12-14T11:07:25.699Z","retestDate":"2023-12-14T03:04:01.908Z","containerId":"meanwhile duh ad","batchNo":"oh furthermore","mfgBatchNo":"wherever important","lotNoStr":"while fibre drat","binNumber":"kowtow abaft","comments":"pricey","quantityOnHandTotal":9508.98,"availableToPromiseTotal":3737.93,"accountingQuantityTotal":27847.7,"oldQuantityOnHand":14235.51,"oldAvailableToPromise":6834.54,"serialNumber":"yowza pasta oof","softIdentifier":"velocity","activationNumber":"unibody frenetically psst","activationValidThru":"2023-12-14T22:04:32.418Z","unitCost":8199.57},
    }).then(({ body }) => {
      inventoryItem = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/inventory-transfers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/inventory-transfers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/inventory-transfers/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/inventory-items', {
      statusCode: 200,
      body: [inventoryItem],
    });

    cy.intercept('GET', '/api/statuses', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/facilities', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/facility-locations', {
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
    if (inventoryTransfer) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/inventory-transfers/${inventoryTransfer.id}`,
      }).then(() => {
        inventoryTransfer = undefined;
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

  it('InventoryTransfers menu should load InventoryTransfers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('inventory-transfer');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InventoryTransfer').should('exist');
    cy.location('pathname').should('eq', inventoryTransferPageUrl);
  });

  describe('InventoryTransfer page', () => {
    it('should have translated page title', () => {
      cy.visit(inventoryTransferPageUrl);
      cy.getEntityHeading('InventoryTransfer').should('not.contain', 'snpApp.inventoryTransfer.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(inventoryTransferPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InventoryTransfer page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${inventoryTransferPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('InventoryTransfer');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventoryTransferPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/inventory-transfers',
          body: {
            ...inventoryTransferSample,
            inventoryItem: inventoryItem,
          },
        }).then(({ body }) => {
          inventoryTransfer = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/inventory-transfers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/inventory-transfers?page=0&size=20>; rel="last",<http://localhost/api/inventory-transfers?page=0&size=20>; rel="first"',
              },
              body: [inventoryTransfer],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(inventoryTransferPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(inventoryTransferPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details InventoryTransfer page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('inventoryTransfer');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventoryTransferPageUrl);
      });

      it('edit button click should load edit InventoryTransfer page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InventoryTransfer');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventoryTransferPageUrl);
      });

      it('edit button click should load edit InventoryTransfer page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InventoryTransfer');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventoryTransferPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of InventoryTransfer', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('inventoryTransfer').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventoryTransferPageUrl);

        inventoryTransfer = undefined;
      });
    });
  });

  describe('new InventoryTransfer page', () => {
    beforeEach(() => {
      cy.visit(inventoryTransferPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('InventoryTransfer');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of InventoryTransfer', () => {
      cy.get(`[data-cy="sentDate"]`).type('2023-12-14T17:07');
      cy.get(`[data-cy="sentDate"]`).blur();
      cy.get(`[data-cy="sentDate"]`).should('have.value', '2023-12-14T17:07');

      cy.get(`[data-cy="receivedDate"]`).type('2023-12-14T06:27');
      cy.get(`[data-cy="receivedDate"]`).blur();
      cy.get(`[data-cy="receivedDate"]`).should('have.value', '2023-12-14T06:27');

      cy.get(`[data-cy="comments"]`).type('meh');
      cy.get(`[data-cy="comments"]`).should('have.value', 'meh');

      cy.get(`[data-cy="inventoryItem"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        inventoryTransfer = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', inventoryTransferPageUrl);
    });
  });
});
