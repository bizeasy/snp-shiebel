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

describe('ProductFeatureApplType e2e test', () => {
  const productFeatureApplTypePageUrl = '/product-feature-appl-type';
  let username: string;
  let password: string;
  const productFeatureApplTypeSample = { name: 'sustenance' };

  let productFeatureApplType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-feature-appl-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-feature-appl-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-feature-appl-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productFeatureApplType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-feature-appl-types/${productFeatureApplType.id}`,
      }).then(() => {
        productFeatureApplType = undefined;
      });
    }
  });

  it('ProductFeatureApplTypes menu should load ProductFeatureApplTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-feature-appl-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductFeatureApplType').should('exist');
    cy.location('pathname').should('eq', productFeatureApplTypePageUrl);
  });

  describe('ProductFeatureApplType page', () => {
    it('should have translated page title', () => {
      cy.visit(productFeatureApplTypePageUrl);
      cy.getEntityHeading('ProductFeatureApplType').should('not.contain', 'snpApp.productFeatureApplType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productFeatureApplTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductFeatureApplType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${productFeatureApplTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ProductFeatureApplType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureApplTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-feature-appl-types',
          body: productFeatureApplTypeSample,
        }).then(({ body }) => {
          productFeatureApplType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-feature-appl-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productFeatureApplType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productFeatureApplTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductFeatureApplType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productFeatureApplType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureApplTypePageUrl);
      });

      it('edit button click should load edit ProductFeatureApplType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductFeatureApplType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureApplTypePageUrl);
      });

      it('edit button click should load edit ProductFeatureApplType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductFeatureApplType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureApplTypePageUrl);
      });

      it('last delete button click should delete instance of ProductFeatureApplType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productFeatureApplType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureApplTypePageUrl);

        productFeatureApplType = undefined;
      });
    });
  });

  describe('new ProductFeatureApplType page', () => {
    beforeEach(() => {
      cy.visit(productFeatureApplTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductFeatureApplType');
    });

    it('should create an instance of ProductFeatureApplType', () => {
      cy.get(`[data-cy="name"]`).type('range');
      cy.get(`[data-cy="name"]`).should('have.value', 'range');

      cy.get(`[data-cy="description"]`).type('uh-huh');
      cy.get(`[data-cy="description"]`).should('have.value', 'uh-huh');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productFeatureApplType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', productFeatureApplTypePageUrl);
    });
  });
});
