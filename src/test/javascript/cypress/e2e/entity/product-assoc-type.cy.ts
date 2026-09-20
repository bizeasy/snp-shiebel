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

describe('ProductAssocType e2e test', () => {
  const productAssocTypePageUrl = '/product-assoc-type';
  let username: string;
  let password: string;
  const productAssocTypeSample = { name: 'naturally proceed' };

  let productAssocType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-assoc-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-assoc-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-assoc-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productAssocType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-assoc-types/${productAssocType.id}`,
      }).then(() => {
        productAssocType = undefined;
      });
    }
  });

  it('ProductAssocTypes menu should load ProductAssocTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-assoc-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductAssocType').should('exist');
    cy.location('pathname').should('eq', productAssocTypePageUrl);
  });

  describe('ProductAssocType page', () => {
    it('should have translated page title', () => {
      cy.visit(productAssocTypePageUrl);
      cy.getEntityHeading('ProductAssocType').should('not.contain', 'snpApp.productAssocType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productAssocTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductAssocType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${productAssocTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ProductAssocType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productAssocTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-assoc-types',
          body: productAssocTypeSample,
        }).then(({ body }) => {
          productAssocType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-assoc-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productAssocType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productAssocTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductAssocType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productAssocType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productAssocTypePageUrl);
      });

      it('edit button click should load edit ProductAssocType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductAssocType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productAssocTypePageUrl);
      });

      it('edit button click should load edit ProductAssocType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductAssocType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productAssocTypePageUrl);
      });

      it('last delete button click should delete instance of ProductAssocType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productAssocType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productAssocTypePageUrl);

        productAssocType = undefined;
      });
    });
  });

  describe('new ProductAssocType page', () => {
    beforeEach(() => {
      cy.visit(productAssocTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductAssocType');
    });

    it('should create an instance of ProductAssocType', () => {
      cy.get(`[data-cy="name"]`).type('weird about');
      cy.get(`[data-cy="name"]`).should('have.value', 'weird about');

      cy.get(`[data-cy="description"]`).type('around that');
      cy.get(`[data-cy="description"]`).should('have.value', 'around that');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productAssocType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', productAssocTypePageUrl);
    });
  });
});
