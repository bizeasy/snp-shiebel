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

describe('ProductCategoryType e2e test', () => {
  const productCategoryTypePageUrl = '/product-category-type';
  let username: string;
  let password: string;
  const productCategoryTypeSample = { name: 'and babushka yuck' };

  let productCategoryType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-category-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-category-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-category-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productCategoryType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-category-types/${productCategoryType.id}`,
      }).then(() => {
        productCategoryType = undefined;
      });
    }
  });

  it('ProductCategoryTypes menu should load ProductCategoryTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-category-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductCategoryType').should('exist');
    cy.location('pathname').should('eq', productCategoryTypePageUrl);
  });

  describe('ProductCategoryType page', () => {
    it('should have translated page title', () => {
      cy.visit(productCategoryTypePageUrl);
      cy.getEntityHeading('ProductCategoryType').should('not.contain', 'snpApp.productCategoryType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productCategoryTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductCategoryType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${productCategoryTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ProductCategoryType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productCategoryTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-category-types',
          body: productCategoryTypeSample,
        }).then(({ body }) => {
          productCategoryType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-category-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productCategoryType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productCategoryTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductCategoryType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productCategoryType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productCategoryTypePageUrl);
      });

      it('edit button click should load edit ProductCategoryType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductCategoryType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productCategoryTypePageUrl);
      });

      it('edit button click should load edit ProductCategoryType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductCategoryType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productCategoryTypePageUrl);
      });

      it('last delete button click should delete instance of ProductCategoryType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productCategoryType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productCategoryTypePageUrl);

        productCategoryType = undefined;
      });
    });
  });

  describe('new ProductCategoryType page', () => {
    beforeEach(() => {
      cy.visit(productCategoryTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductCategoryType');
    });

    it('should create an instance of ProductCategoryType', () => {
      cy.get(`[data-cy="name"]`).type('during');
      cy.get(`[data-cy="name"]`).should('have.value', 'during');

      cy.get(`[data-cy="description"]`).type('receptor now hello');
      cy.get(`[data-cy="description"]`).should('have.value', 'receptor now hello');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productCategoryType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', productCategoryTypePageUrl);
    });
  });
});
