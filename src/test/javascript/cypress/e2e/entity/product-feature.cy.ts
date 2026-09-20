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

describe('ProductFeature e2e test', () => {
  const productFeaturePageUrl = '/product-feature';
  let username: string;
  let password: string;
  const productFeatureSample = { name: 'greatly label till' };

  let productFeature;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-features+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-features').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-features/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productFeature) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-features/${productFeature.id}`,
      }).then(() => {
        productFeature = undefined;
      });
    }
  });

  it('ProductFeatures menu should load ProductFeatures page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-feature');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductFeature').should('exist');
    cy.location('pathname').should('eq', productFeaturePageUrl);
  });

  describe('ProductFeature page', () => {
    it('should have translated page title', () => {
      cy.visit(productFeaturePageUrl);
      cy.getEntityHeading('ProductFeature').should('not.contain', 'snpApp.productFeature.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productFeaturePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductFeature page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${productFeaturePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ProductFeature');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeaturePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-features',
          body: productFeatureSample,
        }).then(({ body }) => {
          productFeature = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-features+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/product-features?page=0&size=20>; rel="last",<http://localhost/api/product-features?page=0&size=20>; rel="first"',
              },
              body: [productFeature],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productFeaturePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductFeature page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productFeature');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeaturePageUrl);
      });

      it('edit button click should load edit ProductFeature page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductFeature');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeaturePageUrl);
      });

      it('edit button click should load edit ProductFeature page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductFeature');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeaturePageUrl);
      });

      it('last delete button click should delete instance of ProductFeature', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productFeature').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeaturePageUrl);

        productFeature = undefined;
      });
    });
  });

  describe('new ProductFeature page', () => {
    beforeEach(() => {
      cy.visit(productFeaturePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductFeature');
    });

    it('should create an instance of ProductFeature', () => {
      cy.get(`[data-cy="name"]`).type('unit kiddingly unexpectedly');
      cy.get(`[data-cy="name"]`).should('have.value', 'unit kiddingly unexpectedly');

      cy.get(`[data-cy="description"]`).type('pear rot hmph');
      cy.get(`[data-cy="description"]`).should('have.value', 'pear rot hmph');

      cy.get(`[data-cy="amount"]`).type('29047.36');
      cy.get(`[data-cy="amount"]`).should('have.value', '29047.36');

      cy.get(`[data-cy="sequenceNo"]`).type('4607');
      cy.get(`[data-cy="sequenceNo"]`).should('have.value', '4607');

      cy.get(`[data-cy="abbrev"]`).type('hygienic mythology a');
      cy.get(`[data-cy="abbrev"]`).should('have.value', 'hygienic mythology a');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productFeature = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', productFeaturePageUrl);
    });
  });
});
