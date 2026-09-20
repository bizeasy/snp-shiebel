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

describe('ProductPricePurpose e2e test', () => {
  const productPricePurposePageUrl = '/product-price-purpose';
  let username: string;
  let password: string;
  const productPricePurposeSample = { name: 'hence heartfelt below' };

  let productPricePurpose;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-price-purposes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-price-purposes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-price-purposes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productPricePurpose) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-price-purposes/${productPricePurpose.id}`,
      }).then(() => {
        productPricePurpose = undefined;
      });
    }
  });

  it('ProductPricePurposes menu should load ProductPricePurposes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-price-purpose');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductPricePurpose').should('exist');
    cy.location('pathname').should('eq', productPricePurposePageUrl);
  });

  describe('ProductPricePurpose page', () => {
    it('should have translated page title', () => {
      cy.visit(productPricePurposePageUrl);
      cy.getEntityHeading('ProductPricePurpose').should('not.contain', 'snpApp.productPricePurpose.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productPricePurposePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductPricePurpose page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${productPricePurposePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ProductPricePurpose');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productPricePurposePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-price-purposes',
          body: productPricePurposeSample,
        }).then(({ body }) => {
          productPricePurpose = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-price-purposes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productPricePurpose],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productPricePurposePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductPricePurpose page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productPricePurpose');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productPricePurposePageUrl);
      });

      it('edit button click should load edit ProductPricePurpose page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductPricePurpose');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productPricePurposePageUrl);
      });

      it('edit button click should load edit ProductPricePurpose page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductPricePurpose');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productPricePurposePageUrl);
      });

      it('last delete button click should delete instance of ProductPricePurpose', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productPricePurpose').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productPricePurposePageUrl);

        productPricePurpose = undefined;
      });
    });
  });

  describe('new ProductPricePurpose page', () => {
    beforeEach(() => {
      cy.visit(productPricePurposePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductPricePurpose');
    });

    it('should create an instance of ProductPricePurpose', () => {
      cy.get(`[data-cy="name"]`).type('ah aw');
      cy.get(`[data-cy="name"]`).should('have.value', 'ah aw');

      cy.get(`[data-cy="description"]`).type('confute');
      cy.get(`[data-cy="description"]`).should('have.value', 'confute');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productPricePurpose = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', productPricePurposePageUrl);
    });
  });
});
