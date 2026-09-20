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

describe('InventoryItem e2e test', () => {
  const inventoryItemPageUrl = '/inventory-item';
  let username: string;
  let password: string;
  const inventoryItemSample = {};

  let inventoryItem;
  let product;

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
      url: '/api/products',
      body: {
        name: 'second',
        internalName: 'into but',
        brandName: 'feminize',
        variant: 'crocodile unlike',
        sku: 'whose',
        introductionDate: '2023-12-14T15:11:43.781Z',
        releaseDate: '2023-12-14T21:23:27.543Z',
        quantityIncluded: 1270.68,
        piecesIncluded: 11747,
        weight: 27677.25,
        height: 23609.8,
        width: 24172.75,
        depth: 3134.82,
        smallImageUrl: 'steep',
        mediumImageUrl: 'hidden anenst modulo',
        largeImageUrl: 'delete',
        detailImageUrl: 'ugh habit notwithstanding',
        originalImageUrl: 'modulo ah',
        altImageUrl1: 'agile',
        altImageUrl2: 'rag whoever',
        altImageUrl3: 'internalize',
        altImageUrl4: 'kookily continually gosh',
        altImageUrl5: 'conclude via orient',
        altImageUrl6: 'viability very off',
        videoUrl: 'indeed whispered',
        quantity: 20783.64,
        price: 22089.82,
        cgst: 28591.91,
        igst: 12443.56,
        sgst: 25651.82,
        cgstPercentage: 11295.58,
        igstPercentage: 22076.95,
        sgstPercentage: 24711.94,
        totalPrice: 17446.38,
        description: 'other for',
        longDescription: 'settler quicker',
        info: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        isVirtual: true,
        isVariant: false,
        requireInventory: true,
        returnable: false,
        isPopular: false,
        isBanner: true,
        bannerUrl: 'when oof',
      },
    }).then(({ body }) => {
      product = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/inventory-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/inventory-items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/inventory-items/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/inventory-item-types', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [product],
    });

    cy.intercept('GET', '/api/parties', {
      statusCode: 200,
      body: [],
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

    cy.intercept('GET', '/api/uoms', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/lots', {
      statusCode: 200,
      body: [],
    });
  });

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

  afterEach(() => {
    if (product) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/products/${product.id}`,
      }).then(() => {
        product = undefined;
      });
    }
  });

  it('InventoryItems menu should load InventoryItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('inventory-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InventoryItem').should('exist');
    cy.location('pathname').should('eq', inventoryItemPageUrl);
  });

  describe('InventoryItem page', () => {
    it('should have translated page title', () => {
      cy.visit(inventoryItemPageUrl);
      cy.getEntityHeading('InventoryItem').should('not.contain', 'snpApp.inventoryItem.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(inventoryItemPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InventoryItem page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${inventoryItemPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('InventoryItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventoryItemPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/inventory-items',
          body: {
            ...inventoryItemSample,
            product,
          },
        }).then(({ body }) => {
          inventoryItem = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/inventory-items+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/inventory-items?page=0&size=20>; rel="last",<http://localhost/api/inventory-items?page=0&size=20>; rel="first"',
              },
              body: [inventoryItem],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(inventoryItemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details InventoryItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('inventoryItem');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventoryItemPageUrl);
      });

      it('edit button click should load edit InventoryItem page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InventoryItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventoryItemPageUrl);
      });

      it('edit button click should load edit InventoryItem page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InventoryItem');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventoryItemPageUrl);
      });

      it('last delete button click should delete instance of InventoryItem', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('inventoryItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventoryItemPageUrl);

        inventoryItem = undefined;
      });
    });
  });

  describe('new InventoryItem page', () => {
    beforeEach(() => {
      cy.visit(inventoryItemPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('InventoryItem');
    });

    it('should create an instance of InventoryItem', () => {
      cy.get(`[data-cy="receivedDate"]`).type('2023-12-14T09:33');
      cy.get(`[data-cy="receivedDate"]`).blur();
      cy.get(`[data-cy="receivedDate"]`).should('have.value', '2023-12-14T09:33');

      cy.get(`[data-cy="manufacturedDate"]`).type('2023-12-14T19:37');
      cy.get(`[data-cy="manufacturedDate"]`).blur();
      cy.get(`[data-cy="manufacturedDate"]`).should('have.value', '2023-12-14T19:37');

      cy.get(`[data-cy="expiryDate"]`).type('2023-12-14T20:39');
      cy.get(`[data-cy="expiryDate"]`).blur();
      cy.get(`[data-cy="expiryDate"]`).should('have.value', '2023-12-14T20:39');

      cy.get(`[data-cy="retestDate"]`).type('2023-12-14T02:37');
      cy.get(`[data-cy="retestDate"]`).blur();
      cy.get(`[data-cy="retestDate"]`).should('have.value', '2023-12-14T02:37');

      cy.get(`[data-cy="containerId"]`).type('until what');
      cy.get(`[data-cy="containerId"]`).should('have.value', 'until what');

      cy.get(`[data-cy="batchNo"]`).type('unlike thick before');
      cy.get(`[data-cy="batchNo"]`).should('have.value', 'unlike thick before');

      cy.get(`[data-cy="mfgBatchNo"]`).type('where');
      cy.get(`[data-cy="mfgBatchNo"]`).should('have.value', 'where');

      cy.get(`[data-cy="lotNoStr"]`).type('license before');
      cy.get(`[data-cy="lotNoStr"]`).should('have.value', 'license before');

      cy.get(`[data-cy="binNumber"]`).type('boo whoever');
      cy.get(`[data-cy="binNumber"]`).should('have.value', 'boo whoever');

      cy.get(`[data-cy="comments"]`).type('alongside gloss');
      cy.get(`[data-cy="comments"]`).should('have.value', 'alongside gloss');

      cy.get(`[data-cy="quantityOnHandTotal"]`).type('1431.69');
      cy.get(`[data-cy="quantityOnHandTotal"]`).should('have.value', '1431.69');

      cy.get(`[data-cy="availableToPromiseTotal"]`).type('23609.53');
      cy.get(`[data-cy="availableToPromiseTotal"]`).should('have.value', '23609.53');

      cy.get(`[data-cy="accountingQuantityTotal"]`).type('31898.04');
      cy.get(`[data-cy="accountingQuantityTotal"]`).should('have.value', '31898.04');

      cy.get(`[data-cy="oldQuantityOnHand"]`).type('19453.19');
      cy.get(`[data-cy="oldQuantityOnHand"]`).should('have.value', '19453.19');

      cy.get(`[data-cy="oldAvailableToPromise"]`).type('12291.33');
      cy.get(`[data-cy="oldAvailableToPromise"]`).should('have.value', '12291.33');

      cy.get(`[data-cy="serialNumber"]`).type('disbar');
      cy.get(`[data-cy="serialNumber"]`).should('have.value', 'disbar');

      cy.get(`[data-cy="softIdentifier"]`).type('incidentally');
      cy.get(`[data-cy="softIdentifier"]`).should('have.value', 'incidentally');

      cy.get(`[data-cy="activationNumber"]`).type('satirise');
      cy.get(`[data-cy="activationNumber"]`).should('have.value', 'satirise');

      cy.get(`[data-cy="activationValidThru"]`).type('2023-12-14T17:16');
      cy.get(`[data-cy="activationValidThru"]`).blur();
      cy.get(`[data-cy="activationValidThru"]`).should('have.value', '2023-12-14T17:16');

      cy.get(`[data-cy="unitCost"]`).type('10250.18');
      cy.get(`[data-cy="unitCost"]`).should('have.value', '10250.18');

      cy.get(`[data-cy="product"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        inventoryItem = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', inventoryItemPageUrl);
    });
  });
});
