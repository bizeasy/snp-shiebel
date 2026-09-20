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

describe('ProductFeatureGroup e2e test', () => {
  const productFeatureGroupPageUrl = '/product-feature-group';
  let username: string;
  let password: string;
  const productFeatureGroupSample = { name: 'provided' };

  let productFeatureGroup;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-feature-groups+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-feature-groups').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-feature-groups/*').as('deleteEntityRequest');
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
  });

  it('ProductFeatureGroups menu should load ProductFeatureGroups page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-feature-group');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductFeatureGroup').should('exist');
    cy.location('pathname').should('eq', productFeatureGroupPageUrl);
  });

  describe('ProductFeatureGroup page', () => {
    it('should have translated page title', () => {
      cy.visit(productFeatureGroupPageUrl);
      cy.getEntityHeading('ProductFeatureGroup').should('not.contain', 'snpApp.productFeatureGroup.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productFeatureGroupPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductFeatureGroup page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${productFeatureGroupPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ProductFeatureGroup');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureGroupPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-feature-groups',
          body: productFeatureGroupSample,
        }).then(({ body }) => {
          productFeatureGroup = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-feature-groups+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productFeatureGroup],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productFeatureGroupPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductFeatureGroup page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productFeatureGroup');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureGroupPageUrl);
      });

      it('edit button click should load edit ProductFeatureGroup page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductFeatureGroup');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureGroupPageUrl);
      });

      it('edit button click should load edit ProductFeatureGroup page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductFeatureGroup');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureGroupPageUrl);
      });

      it('last delete button click should delete instance of ProductFeatureGroup', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productFeatureGroup').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureGroupPageUrl);

        productFeatureGroup = undefined;
      });
    });
  });

  describe('new ProductFeatureGroup page', () => {
    beforeEach(() => {
      cy.visit(productFeatureGroupPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductFeatureGroup');
    });

    it('should create an instance of ProductFeatureGroup', () => {
      cy.get(`[data-cy="name"]`).type('knife farmer unimportant');
      cy.get(`[data-cy="name"]`).should('have.value', 'knife farmer unimportant');

      cy.get(`[data-cy="description"]`).type('charlatan');
      cy.get(`[data-cy="description"]`).should('have.value', 'charlatan');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productFeatureGroup = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', productFeatureGroupPageUrl);
    });
  });
});
