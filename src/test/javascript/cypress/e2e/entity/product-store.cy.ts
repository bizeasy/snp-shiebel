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

describe('ProductStore e2e test', () => {
  const productStorePageUrl = '/product-store';
  let username: string;
  let password: string;
  const productStoreSample = { name: 'tromp reprimand' };

  let productStore;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-stores+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-stores').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-stores/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productStore) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-stores/${productStore.id}`,
      }).then(() => {
        productStore = undefined;
      });
    }
  });

  it('ProductStores menu should load ProductStores page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-store');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductStore').should('exist');
    cy.location('pathname').should('eq', productStorePageUrl);
  });

  describe('ProductStore page', () => {
    it('should have translated page title', () => {
      cy.visit(productStorePageUrl);
      cy.getEntityHeading('ProductStore').should('not.contain', 'snpApp.productStore.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productStorePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductStore page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${productStorePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ProductStore');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productStorePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-stores',
          body: productStoreSample,
        }).then(({ body }) => {
          productStore = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-stores+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productStore],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productStorePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductStore page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productStore');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productStorePageUrl);
      });

      it('edit button click should load edit ProductStore page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductStore');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productStorePageUrl);
      });

      it('edit button click should load edit ProductStore page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductStore');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productStorePageUrl);
      });

      it('last delete button click should delete instance of ProductStore', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productStore').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productStorePageUrl);

        productStore = undefined;
      });
    });
  });

  describe('new ProductStore page', () => {
    beforeEach(() => {
      cy.visit(productStorePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductStore');
    });

    it('should create an instance of ProductStore', () => {
      cy.get(`[data-cy="name"]`).type('reboot anti');
      cy.get(`[data-cy="name"]`).should('have.value', 'reboot anti');

      cy.get(`[data-cy="title"]`).type('yowza who uncommon');
      cy.get(`[data-cy="title"]`).should('have.value', 'yowza who uncommon');

      cy.get(`[data-cy="postalAddress"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="postalAddress"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productStore = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', productStorePageUrl);
    });
  });
});
