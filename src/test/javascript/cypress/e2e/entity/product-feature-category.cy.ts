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

describe('ProductFeatureCategory e2e test', () => {
  const productFeatureCategoryPageUrl = '/product-feature-category';
  let username: string;
  let password: string;
  const productFeatureCategorySample = { name: 'case scoop' };

  let productFeatureCategory;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-feature-categories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-feature-categories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-feature-categories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productFeatureCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-feature-categories/${productFeatureCategory.id}`,
      }).then(() => {
        productFeatureCategory = undefined;
      });
    }
  });

  it('ProductFeatureCategories menu should load ProductFeatureCategories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-feature-category');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductFeatureCategory').should('exist');
    cy.location('pathname').should('eq', productFeatureCategoryPageUrl);
  });

  describe('ProductFeatureCategory page', () => {
    it('should have translated page title', () => {
      cy.visit(productFeatureCategoryPageUrl);
      cy.getEntityHeading('ProductFeatureCategory').should('not.contain', 'snpApp.productFeatureCategory.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productFeatureCategoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductFeatureCategory page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${productFeatureCategoryPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ProductFeatureCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureCategoryPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-feature-categories',
          body: productFeatureCategorySample,
        }).then(({ body }) => {
          productFeatureCategory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-feature-categories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productFeatureCategory],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productFeatureCategoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductFeatureCategory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productFeatureCategory');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureCategoryPageUrl);
      });

      it('edit button click should load edit ProductFeatureCategory page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductFeatureCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureCategoryPageUrl);
      });

      it('edit button click should load edit ProductFeatureCategory page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductFeatureCategory');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureCategoryPageUrl);
      });

      it('last delete button click should delete instance of ProductFeatureCategory', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productFeatureCategory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureCategoryPageUrl);

        productFeatureCategory = undefined;
      });
    });
  });

  describe('new ProductFeatureCategory page', () => {
    beforeEach(() => {
      cy.visit(productFeatureCategoryPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductFeatureCategory');
    });

    it('should create an instance of ProductFeatureCategory', () => {
      cy.get(`[data-cy="name"]`).type('finally');
      cy.get(`[data-cy="name"]`).should('have.value', 'finally');

      cy.get(`[data-cy="description"]`).type('instead');
      cy.get(`[data-cy="description"]`).should('have.value', 'instead');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productFeatureCategory = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', productFeatureCategoryPageUrl);
    });
  });
});
