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

describe('ProductCategory e2e test', () => {
  const productCategoryPageUrl = '/product-category';
  let username: string;
  let password: string;
  const productCategorySample = { name: 'fly swath' };

  let productCategory;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-categories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-categories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-categories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-categories/${productCategory.id}`,
      }).then(() => {
        productCategory = undefined;
      });
    }
  });

  it('ProductCategories menu should load ProductCategories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-category');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductCategory').should('exist');
    cy.location('pathname').should('eq', productCategoryPageUrl);
  });

  describe('ProductCategory page', () => {
    it('should have translated page title', () => {
      cy.visit(productCategoryPageUrl);
      cy.getEntityHeading('ProductCategory').should('not.contain', 'snpApp.productCategory.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productCategoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductCategory page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${productCategoryPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ProductCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productCategoryPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-categories',
          body: productCategorySample,
        }).then(({ body }) => {
          productCategory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-categories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productCategory],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productCategoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductCategory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productCategory');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productCategoryPageUrl);
      });

      it('edit button click should load edit ProductCategory page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productCategoryPageUrl);
      });

      it('edit button click should load edit ProductCategory page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductCategory');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productCategoryPageUrl);
      });

      it('last delete button click should delete instance of ProductCategory', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productCategory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productCategoryPageUrl);

        productCategory = undefined;
      });
    });
  });

  describe('new ProductCategory page', () => {
    beforeEach(() => {
      cy.visit(productCategoryPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductCategory');
    });

    it('should create an instance of ProductCategory', () => {
      cy.get(`[data-cy="name"]`).type('slip how jealously');
      cy.get(`[data-cy="name"]`).should('have.value', 'slip how jealously');

      cy.get(`[data-cy="description"]`).type('by');
      cy.get(`[data-cy="description"]`).should('have.value', 'by');

      cy.get(`[data-cy="longDescription"]`).type('harmful partially');
      cy.get(`[data-cy="longDescription"]`).should('have.value', 'harmful partially');

      cy.get(`[data-cy="attribute"]`).type('disposer adventurously un');
      cy.get(`[data-cy="attribute"]`).should('have.value', 'disposer adventurously un');

      cy.get(`[data-cy="sequenceNo"]`).type('2777');
      cy.get(`[data-cy="sequenceNo"]`).should('have.value', '2777');

      cy.get(`[data-cy="imageUrl"]`).type('off while');
      cy.get(`[data-cy="imageUrl"]`).should('have.value', 'off while');

      cy.get(`[data-cy="altImageUrl"]`).type('how furthermore');
      cy.get(`[data-cy="altImageUrl"]`).should('have.value', 'how furthermore');

      cy.get(`[data-cy="bannerUrl"]`).type('unlucky carefully');
      cy.get(`[data-cy="bannerUrl"]`).should('have.value', 'unlucky carefully');

      cy.get(`[data-cy="largeImageUrl"]`).type('despite phew');
      cy.get(`[data-cy="largeImageUrl"]`).should('have.value', 'despite phew');

      cy.get(`[data-cy="altLargeImageUrl"]`).type('except general passionate');
      cy.get(`[data-cy="altLargeImageUrl"]`).should('have.value', 'except general passionate');

      cy.get(`[data-cy="info"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="info"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="isBanner"]`).should('not.be.checked');
      cy.get(`[data-cy="isBanner"]`).click();
      cy.get(`[data-cy="isBanner"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productCategory = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', productCategoryPageUrl);
    });
  });
});
