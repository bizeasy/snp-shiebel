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

describe('ProductFeatureCategoryAppl e2e test', () => {
  const productFeatureCategoryApplPageUrl = '/product-feature-category-appl';
  let username: string;
  let password: string;
  const productFeatureCategoryApplSample = { fromDate: '2023-12-14T09:00:58.426Z' };

  let productFeatureCategoryAppl;
  let productCategory;
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
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/product-categories',
      body: {
        name: 'tinted',
        description: 'blaring pfft enchanted',
        longDescription: 'gosh midst',
        attribute: 'coagulate',
        sequenceNo: 31591,
        imageUrl: 'ravage woefully before',
        altImageUrl: 'for',
        bannerUrl: 'duh',
        largeImageUrl: 'before measly',
        altLargeImageUrl: 'inside to',
        info: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        isBanner: false,
      },
    }).then(({ body }) => {
      productCategory = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/product-feature-categories',
      body: { name: 'verify', description: 'gracefully oof oh' },
    }).then(({ body }) => {
      productFeatureCategory = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-feature-category-appls+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-feature-category-appls').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-feature-category-appls/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/product-categories', {
      statusCode: 200,
      body: [productCategory],
    });

    cy.intercept('GET', '/api/product-feature-categories', {
      statusCode: 200,
      body: [productFeatureCategory],
    });
  });

  afterEach(() => {
    if (productFeatureCategoryAppl) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-feature-category-appls/${productFeatureCategoryAppl.id}`,
      }).then(() => {
        productFeatureCategoryAppl = undefined;
      });
    }
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
    if (productFeatureCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-feature-categories/${productFeatureCategory.id}`,
      }).then(() => {
        productFeatureCategory = undefined;
      });
    }
  });

  it('ProductFeatureCategoryAppls menu should load ProductFeatureCategoryAppls page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-feature-category-appl');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductFeatureCategoryAppl').should('exist');
    cy.location('pathname').should('eq', productFeatureCategoryApplPageUrl);
  });

  describe('ProductFeatureCategoryAppl page', () => {
    it('should have translated page title', () => {
      cy.visit(productFeatureCategoryApplPageUrl);
      cy.getEntityHeading('ProductFeatureCategoryAppl').should('not.contain', 'snpApp.productFeatureCategoryAppl.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productFeatureCategoryApplPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductFeatureCategoryAppl page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${productFeatureCategoryApplPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ProductFeatureCategoryAppl');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureCategoryApplPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-feature-category-appls',
          body: {
            ...productFeatureCategoryApplSample,
            productCategory,
            productFeatureCategory,
          },
        }).then(({ body }) => {
          productFeatureCategoryAppl = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-feature-category-appls+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/product-feature-category-appls?page=0&size=20>; rel="last",<http://localhost/api/product-feature-category-appls?page=0&size=20>; rel="first"',
              },
              body: [productFeatureCategoryAppl],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productFeatureCategoryApplPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductFeatureCategoryAppl page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productFeatureCategoryAppl');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureCategoryApplPageUrl);
      });

      it('edit button click should load edit ProductFeatureCategoryAppl page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductFeatureCategoryAppl');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureCategoryApplPageUrl);
      });

      it('edit button click should load edit ProductFeatureCategoryAppl page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductFeatureCategoryAppl');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureCategoryApplPageUrl);
      });

      it('last delete button click should delete instance of ProductFeatureCategoryAppl', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productFeatureCategoryAppl').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureCategoryApplPageUrl);

        productFeatureCategoryAppl = undefined;
      });
    });
  });

  describe('new ProductFeatureCategoryAppl page', () => {
    beforeEach(() => {
      cy.visit(productFeatureCategoryApplPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductFeatureCategoryAppl');
    });

    it('should create an instance of ProductFeatureCategoryAppl', () => {
      cy.get(`[data-cy="fromDate"]`).type('2023-12-14T17:48');
      cy.get(`[data-cy="fromDate"]`).blur();
      cy.get(`[data-cy="fromDate"]`).should('have.value', '2023-12-14T17:48');

      cy.get(`[data-cy="thruDate"]`).type('2023-12-14T08:56');
      cy.get(`[data-cy="thruDate"]`).blur();
      cy.get(`[data-cy="thruDate"]`).should('have.value', '2023-12-14T08:56');

      cy.get(`[data-cy="productCategory"]`).select(1);
      cy.get(`[data-cy="productFeatureCategory"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productFeatureCategoryAppl = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', productFeatureCategoryApplPageUrl);
    });
  });
});
