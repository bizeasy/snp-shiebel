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

describe('InventoryItemDelegate e2e test', () => {
  const inventoryItemDelegatePageUrl = '/inventory-item-delegate';
  let username: string;
  let password: string;
  // const inventoryItemDelegateSample = {"effectiveDate":"2023-12-14T09:33:23.734Z"};

  let inventoryItemDelegate;
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
      body: {"receivedDate":"2023-12-14T17:46:34.981Z","manufacturedDate":"2023-12-14T00:22:07.424Z","expiryDate":"2023-12-14T15:29:50.908Z","retestDate":"2023-12-14T05:41:02.370Z","containerId":"adrenalin amid","batchNo":"boohoo","mfgBatchNo":"hmph unfortunately","lotNoStr":"ameliorate","binNumber":"since","comments":"reassuringly as huzzah","quantityOnHandTotal":26436.33,"availableToPromiseTotal":1058.68,"accountingQuantityTotal":3838.32,"oldQuantityOnHand":4752.83,"oldAvailableToPromise":16901.6,"serialNumber":"trench redress provided","softIdentifier":"plain whose jacket","activationNumber":"necessary ick","activationValidThru":"2023-12-14T02:17:06.550Z","unitCost":31550.94},
    }).then(({ body }) => {
      inventoryItem = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/inventory-item-delegates+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/inventory-item-delegates').as('postEntityRequest');
    cy.intercept('DELETE', '/api/inventory-item-delegates/*').as('deleteEntityRequest');
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

    cy.intercept('GET', '/api/invoices', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/invoice-items', {
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

    cy.intercept('GET', '/api/item-issuances', {
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
    if (inventoryItemDelegate) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/inventory-item-delegates/${inventoryItemDelegate.id}`,
      }).then(() => {
        inventoryItemDelegate = undefined;
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

  it('InventoryItemDelegates menu should load InventoryItemDelegates page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('inventory-item-delegate');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InventoryItemDelegate').should('exist');
    cy.location('pathname').should('eq', inventoryItemDelegatePageUrl);
  });

  describe('InventoryItemDelegate page', () => {
    it('should have translated page title', () => {
      cy.visit(inventoryItemDelegatePageUrl);
      cy.getEntityHeading('InventoryItemDelegate').should('not.contain', 'snpApp.inventoryItemDelegate.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(inventoryItemDelegatePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InventoryItemDelegate page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${inventoryItemDelegatePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('InventoryItemDelegate');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventoryItemDelegatePageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/inventory-item-delegates',
          body: {
            ...inventoryItemDelegateSample,
            inventoryItem: inventoryItem,
          },
        }).then(({ body }) => {
          inventoryItemDelegate = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/inventory-item-delegates+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/inventory-item-delegates?page=0&size=20>; rel="last",<http://localhost/api/inventory-item-delegates?page=0&size=20>; rel="first"',
              },
              body: [inventoryItemDelegate],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(inventoryItemDelegatePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(inventoryItemDelegatePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details InventoryItemDelegate page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('inventoryItemDelegate');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventoryItemDelegatePageUrl);
      });

      it('edit button click should load edit InventoryItemDelegate page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InventoryItemDelegate');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventoryItemDelegatePageUrl);
      });

      it('edit button click should load edit InventoryItemDelegate page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InventoryItemDelegate');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventoryItemDelegatePageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of InventoryItemDelegate', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('inventoryItemDelegate').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventoryItemDelegatePageUrl);

        inventoryItemDelegate = undefined;
      });
    });
  });

  describe('new InventoryItemDelegate page', () => {
    beforeEach(() => {
      cy.visit(inventoryItemDelegatePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('InventoryItemDelegate');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of InventoryItemDelegate', () => {
      cy.get(`[data-cy="sequenceNo"]`).type('9300');
      cy.get(`[data-cy="sequenceNo"]`).should('have.value', '9300');

      cy.get(`[data-cy="effectiveDate"]`).type('2023-12-14T19:45');
      cy.get(`[data-cy="effectiveDate"]`).blur();
      cy.get(`[data-cy="effectiveDate"]`).should('have.value', '2023-12-14T19:45');

      cy.get(`[data-cy="quantityOnHandDiff"]`).type('19328.52');
      cy.get(`[data-cy="quantityOnHandDiff"]`).should('have.value', '19328.52');

      cy.get(`[data-cy="availableToPromiseDiff"]`).type('5466.89');
      cy.get(`[data-cy="availableToPromiseDiff"]`).should('have.value', '5466.89');

      cy.get(`[data-cy="accountingQuantityDiff"]`).type('17125.12');
      cy.get(`[data-cy="accountingQuantityDiff"]`).should('have.value', '17125.12');

      cy.get(`[data-cy="unitCost"]`).type('19591.67');
      cy.get(`[data-cy="unitCost"]`).should('have.value', '19591.67');

      cy.get(`[data-cy="remarks"]`).type('finally wherever except');
      cy.get(`[data-cy="remarks"]`).should('have.value', 'finally wherever except');

      cy.get(`[data-cy="inventoryItem"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        inventoryItemDelegate = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', inventoryItemDelegatePageUrl);
    });
  });
});
