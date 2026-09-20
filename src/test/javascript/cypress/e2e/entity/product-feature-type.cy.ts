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

describe('ProductFeatureType e2e test', () => {
  const productFeatureTypePageUrl = '/product-feature-type';
  let username: string;
  let password: string;
  const productFeatureTypeSample = { name: 'silently best-seller but' };

  let productFeatureType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-feature-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-feature-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-feature-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productFeatureType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-feature-types/${productFeatureType.id}`,
      }).then(() => {
        productFeatureType = undefined;
      });
    }
  });

  it('ProductFeatureTypes menu should load ProductFeatureTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-feature-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductFeatureType').should('exist');
    cy.location('pathname').should('eq', productFeatureTypePageUrl);
  });

  describe('ProductFeatureType page', () => {
    it('should have translated page title', () => {
      cy.visit(productFeatureTypePageUrl);
      cy.getEntityHeading('ProductFeatureType').should('not.contain', 'snpApp.productFeatureType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productFeatureTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductFeatureType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${productFeatureTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ProductFeatureType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-feature-types',
          body: productFeatureTypeSample,
        }).then(({ body }) => {
          productFeatureType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-feature-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productFeatureType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productFeatureTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductFeatureType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productFeatureType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureTypePageUrl);
      });

      it('edit button click should load edit ProductFeatureType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductFeatureType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureTypePageUrl);
      });

      it('edit button click should load edit ProductFeatureType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductFeatureType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureTypePageUrl);
      });

      it('last delete button click should delete instance of ProductFeatureType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productFeatureType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureTypePageUrl);

        productFeatureType = undefined;
      });
    });
  });

  describe('new ProductFeatureType page', () => {
    beforeEach(() => {
      cy.visit(productFeatureTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductFeatureType');
    });

    it('should create an instance of ProductFeatureType', () => {
      cy.get(`[data-cy="name"]`).type('burgeon zowie clonk');
      cy.get(`[data-cy="name"]`).should('have.value', 'burgeon zowie clonk');

      cy.get(`[data-cy="description"]`).type('daintily gah');
      cy.get(`[data-cy="description"]`).should('have.value', 'daintily gah');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productFeatureType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', productFeatureTypePageUrl);
    });
  });
});
