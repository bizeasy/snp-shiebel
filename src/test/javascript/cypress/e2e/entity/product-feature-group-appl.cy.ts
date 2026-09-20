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

describe('ProductFeatureGroupAppl e2e test', () => {
  const productFeatureGroupApplPageUrl = '/product-feature-group-appl';
  let username: string;
  let password: string;
  const productFeatureGroupApplSample = { fromDate: '2023-12-14T20:37:18.932Z' };

  let productFeatureGroupAppl;
  let productFeatureGroup;
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
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/product-feature-groups',
      body: { name: 'nutritious badly', description: 'aha language scout' },
    }).then(({ body }) => {
      productFeatureGroup = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/product-features',
      body: { name: 'noon rewrite', description: 'sway', amount: 12071.7, sequenceNo: 15562, abbrev: 'pfft uniform' },
    }).then(({ body }) => {
      productFeature = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-feature-group-appls+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-feature-group-appls').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-feature-group-appls/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/product-feature-groups', {
      statusCode: 200,
      body: [productFeatureGroup],
    });

    cy.intercept('GET', '/api/product-features', {
      statusCode: 200,
      body: [productFeature],
    });
  });

  afterEach(() => {
    if (productFeatureGroupAppl) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-feature-group-appls/${productFeatureGroupAppl.id}`,
      }).then(() => {
        productFeatureGroupAppl = undefined;
      });
    }
  });

  afterEach(() => {
    if (productFeatureGroup) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-feature-groups/${productFeatureGroup.id}`,
      }).then(() => {
        productFeatureGroup = undefined;
      });
    }
    if (productFeature) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-features/${productFeature.id}`,
      }).then(() => {
        productFeature = undefined;
      });
    }
  });

  it('ProductFeatureGroupAppls menu should load ProductFeatureGroupAppls page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-feature-group-appl');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductFeatureGroupAppl').should('exist');
    cy.location('pathname').should('eq', productFeatureGroupApplPageUrl);
  });

  describe('ProductFeatureGroupAppl page', () => {
    it('should have translated page title', () => {
      cy.visit(productFeatureGroupApplPageUrl);
      cy.getEntityHeading('ProductFeatureGroupAppl').should('not.contain', 'snpApp.productFeatureGroupAppl.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productFeatureGroupApplPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductFeatureGroupAppl page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${productFeatureGroupApplPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ProductFeatureGroupAppl');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureGroupApplPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-feature-group-appls',
          body: {
            ...productFeatureGroupApplSample,
            productFeatureGroup,
            productFeature,
          },
        }).then(({ body }) => {
          productFeatureGroupAppl = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-feature-group-appls+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/product-feature-group-appls?page=0&size=20>; rel="last",<http://localhost/api/product-feature-group-appls?page=0&size=20>; rel="first"',
              },
              body: [productFeatureGroupAppl],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productFeatureGroupApplPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductFeatureGroupAppl page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productFeatureGroupAppl');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureGroupApplPageUrl);
      });

      it('edit button click should load edit ProductFeatureGroupAppl page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductFeatureGroupAppl');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureGroupApplPageUrl);
      });

      it('edit button click should load edit ProductFeatureGroupAppl page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductFeatureGroupAppl');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureGroupApplPageUrl);
      });

      it('last delete button click should delete instance of ProductFeatureGroupAppl', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productFeatureGroupAppl').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureGroupApplPageUrl);

        productFeatureGroupAppl = undefined;
      });
    });
  });

  describe('new ProductFeatureGroupAppl page', () => {
    beforeEach(() => {
      cy.visit(productFeatureGroupApplPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductFeatureGroupAppl');
    });

    it('should create an instance of ProductFeatureGroupAppl', () => {
      cy.get(`[data-cy="fromDate"]`).type('2023-12-14T06:34');
      cy.get(`[data-cy="fromDate"]`).blur();
      cy.get(`[data-cy="fromDate"]`).should('have.value', '2023-12-14T06:34');

      cy.get(`[data-cy="thruDate"]`).type('2023-12-14T15:27');
      cy.get(`[data-cy="thruDate"]`).blur();
      cy.get(`[data-cy="thruDate"]`).should('have.value', '2023-12-14T15:27');

      cy.get(`[data-cy="sequenceNo"]`).type('32541');
      cy.get(`[data-cy="sequenceNo"]`).should('have.value', '32541');

      cy.get(`[data-cy="productFeatureGroup"]`).select(1);
      cy.get(`[data-cy="productFeature"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productFeatureGroupAppl = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', productFeatureGroupApplPageUrl);
    });
  });
});
