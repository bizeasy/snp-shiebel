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

describe('ProductType e2e test', () => {
  const productTypePageUrl = '/product-type';
  let username: string;
  let password: string;
  const productTypeSample = { name: 'searchingly' };

  let productType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-types/${productType.id}`,
      }).then(() => {
        productType = undefined;
      });
    }
  });

  it('ProductTypes menu should load ProductTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductType').should('exist');
    cy.location('pathname').should('eq', productTypePageUrl);
  });

  describe('ProductType page', () => {
    it('should have translated page title', () => {
      cy.visit(productTypePageUrl);
      cy.getEntityHeading('ProductType').should('not.contain', 'snpApp.productType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${productTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ProductType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-types',
          body: productTypeSample,
        }).then(({ body }) => {
          productType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productTypePageUrl);
      });

      it('edit button click should load edit ProductType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productTypePageUrl);
      });

      it('edit button click should load edit ProductType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productTypePageUrl);
      });

      it('last delete button click should delete instance of ProductType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productTypePageUrl);

        productType = undefined;
      });
    });
  });

  describe('new ProductType page', () => {
    beforeEach(() => {
      cy.visit(productTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductType');
    });

    it('should create an instance of ProductType', () => {
      cy.get(`[data-cy="name"]`).type('known');
      cy.get(`[data-cy="name"]`).should('have.value', 'known');

      cy.get(`[data-cy="description"]`).type('gah failing');
      cy.get(`[data-cy="description"]`).should('have.value', 'gah failing');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', productTypePageUrl);
    });
  });
});
