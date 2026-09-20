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

describe('InventoryItemVariance e2e test', () => {
  const inventoryItemVariancePageUrl = '/inventory-item-variance';
  let username: string;
  let password: string;
  // const inventoryItemVarianceSample = {};

  let inventoryItemVariance;
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
      body: {"receivedDate":"2023-12-14T15:58:23.505Z","manufacturedDate":"2023-12-14T03:18:14.792Z","expiryDate":"2023-12-14T04:57:25.011Z","retestDate":"2023-12-14T12:10:01.308Z","containerId":"incidentally inasmuch","batchNo":"but ownership outset","mfgBatchNo":"barring respray","lotNoStr":"lined incomparable","binNumber":"unethically loftily around","comments":"gleefully","quantityOnHandTotal":4581.55,"availableToPromiseTotal":22221.11,"accountingQuantityTotal":483.95,"oldQuantityOnHand":29629.56,"oldAvailableToPromise":21793.07,"serialNumber":"puff gosh","softIdentifier":"plus so until","activationNumber":"geez tenderly what","activationValidThru":"2023-12-14T03:18:37.993Z","unitCost":1646.61},
    }).then(({ body }) => {
      inventoryItem = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/inventory-item-variances+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/inventory-item-variances').as('postEntityRequest');
    cy.intercept('DELETE', '/api/inventory-item-variances/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/inventory-items', {
      statusCode: 200,
      body: [inventoryItem],
    });

  });
   */

  afterEach(() => {
    if (inventoryItemVariance) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/inventory-item-variances/${inventoryItemVariance.id}`,
      }).then(() => {
        inventoryItemVariance = undefined;
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

  it('InventoryItemVariances menu should load InventoryItemVariances page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('inventory-item-variance');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InventoryItemVariance').should('exist');
    cy.location('pathname').should('eq', inventoryItemVariancePageUrl);
  });

  describe('InventoryItemVariance page', () => {
    it('should have translated page title', () => {
      cy.visit(inventoryItemVariancePageUrl);
      cy.getEntityHeading('InventoryItemVariance').should('not.contain', 'snpApp.inventoryItemVariance.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(inventoryItemVariancePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InventoryItemVariance page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${inventoryItemVariancePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('InventoryItemVariance');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventoryItemVariancePageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/inventory-item-variances',
          body: {
            ...inventoryItemVarianceSample,
            inventoryItem: inventoryItem,
          },
        }).then(({ body }) => {
          inventoryItemVariance = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/inventory-item-variances+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/inventory-item-variances?page=0&size=20>; rel="last",<http://localhost/api/inventory-item-variances?page=0&size=20>; rel="first"',
              },
              body: [inventoryItemVariance],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(inventoryItemVariancePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(inventoryItemVariancePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details InventoryItemVariance page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('inventoryItemVariance');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventoryItemVariancePageUrl);
      });

      it('edit button click should load edit InventoryItemVariance page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InventoryItemVariance');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventoryItemVariancePageUrl);
      });

      it('edit button click should load edit InventoryItemVariance page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InventoryItemVariance');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventoryItemVariancePageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of InventoryItemVariance', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('inventoryItemVariance').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventoryItemVariancePageUrl);

        inventoryItemVariance = undefined;
      });
    });
  });

  describe('new InventoryItemVariance page', () => {
    beforeEach(() => {
      cy.visit(inventoryItemVariancePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('InventoryItemVariance');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of InventoryItemVariance', () => {
      cy.get(`[data-cy="varianceReason"]`).type('reward whereas contravene');
      cy.get(`[data-cy="varianceReason"]`).should('have.value', 'reward whereas contravene');

      cy.get(`[data-cy="atpVar"]`).type('24512.41');
      cy.get(`[data-cy="atpVar"]`).should('have.value', '24512.41');

      cy.get(`[data-cy="qohVar"]`).type('1995.21');
      cy.get(`[data-cy="qohVar"]`).should('have.value', '1995.21');

      cy.get(`[data-cy="comments"]`).type('certainly for');
      cy.get(`[data-cy="comments"]`).should('have.value', 'certainly for');

      cy.get(`[data-cy="inventoryItem"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        inventoryItemVariance = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', inventoryItemVariancePageUrl);
    });
  });
});
