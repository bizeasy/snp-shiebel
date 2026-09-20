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

describe('InventoryItemType e2e test', () => {
  const inventoryItemTypePageUrl = '/inventory-item-type';
  let username: string;
  let password: string;
  const inventoryItemTypeSample = { name: 'carelessly blind' };

  let inventoryItemType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/inventory-item-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/inventory-item-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/inventory-item-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (inventoryItemType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/inventory-item-types/${inventoryItemType.id}`,
      }).then(() => {
        inventoryItemType = undefined;
      });
    }
  });

  it('InventoryItemTypes menu should load InventoryItemTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('inventory-item-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InventoryItemType').should('exist');
    cy.location('pathname').should('eq', inventoryItemTypePageUrl);
  });

  describe('InventoryItemType page', () => {
    it('should have translated page title', () => {
      cy.visit(inventoryItemTypePageUrl);
      cy.getEntityHeading('InventoryItemType').should('not.contain', 'snpApp.inventoryItemType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(inventoryItemTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InventoryItemType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${inventoryItemTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('InventoryItemType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventoryItemTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/inventory-item-types',
          body: inventoryItemTypeSample,
        }).then(({ body }) => {
          inventoryItemType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/inventory-item-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [inventoryItemType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(inventoryItemTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details InventoryItemType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('inventoryItemType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventoryItemTypePageUrl);
      });

      it('edit button click should load edit InventoryItemType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InventoryItemType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventoryItemTypePageUrl);
      });

      it('edit button click should load edit InventoryItemType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InventoryItemType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventoryItemTypePageUrl);
      });

      it('last delete button click should delete instance of InventoryItemType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('inventoryItemType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventoryItemTypePageUrl);

        inventoryItemType = undefined;
      });
    });
  });

  describe('new InventoryItemType page', () => {
    beforeEach(() => {
      cy.visit(inventoryItemTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('InventoryItemType');
    });

    it('should create an instance of InventoryItemType', () => {
      cy.get(`[data-cy="name"]`).type('lightly');
      cy.get(`[data-cy="name"]`).should('have.value', 'lightly');

      cy.get(`[data-cy="description"]`).type('deform');
      cy.get(`[data-cy="description"]`).should('have.value', 'deform');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        inventoryItemType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', inventoryItemTypePageUrl);
    });
  });
});
