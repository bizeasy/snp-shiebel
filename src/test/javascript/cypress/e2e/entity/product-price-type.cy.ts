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

describe('ProductPriceType e2e test', () => {
  const productPriceTypePageUrl = '/product-price-type';
  let username: string;
  let password: string;
  const productPriceTypeSample = { name: 'drat throbbing than' };

  let productPriceType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-price-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-price-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-price-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productPriceType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-price-types/${productPriceType.id}`,
      }).then(() => {
        productPriceType = undefined;
      });
    }
  });

  it('ProductPriceTypes menu should load ProductPriceTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-price-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductPriceType').should('exist');
    cy.location('pathname').should('eq', productPriceTypePageUrl);
  });

  describe('ProductPriceType page', () => {
    it('should have translated page title', () => {
      cy.visit(productPriceTypePageUrl);
      cy.getEntityHeading('ProductPriceType').should('not.contain', 'snpApp.productPriceType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productPriceTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductPriceType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${productPriceTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ProductPriceType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productPriceTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-price-types',
          body: productPriceTypeSample,
        }).then(({ body }) => {
          productPriceType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-price-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productPriceType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productPriceTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductPriceType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productPriceType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productPriceTypePageUrl);
      });

      it('edit button click should load edit ProductPriceType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductPriceType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productPriceTypePageUrl);
      });

      it('edit button click should load edit ProductPriceType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductPriceType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productPriceTypePageUrl);
      });

      it('last delete button click should delete instance of ProductPriceType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productPriceType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productPriceTypePageUrl);

        productPriceType = undefined;
      });
    });
  });

  describe('new ProductPriceType page', () => {
    beforeEach(() => {
      cy.visit(productPriceTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductPriceType');
    });

    it('should create an instance of ProductPriceType', () => {
      cy.get(`[data-cy="name"]`).type('vacantly against');
      cy.get(`[data-cy="name"]`).should('have.value', 'vacantly against');

      cy.get(`[data-cy="description"]`).type('continually dreamily unlike');
      cy.get(`[data-cy="description"]`).should('have.value', 'continually dreamily unlike');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productPriceType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', productPriceTypePageUrl);
    });
  });
});
