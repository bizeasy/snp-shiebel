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

describe('ShoppingListType e2e test', () => {
  const shoppingListTypePageUrl = '/shopping-list-type';
  let username: string;
  let password: string;
  const shoppingListTypeSample = { name: 'now who' };

  let shoppingListType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/shopping-list-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/shopping-list-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/shopping-list-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (shoppingListType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/shopping-list-types/${shoppingListType.id}`,
      }).then(() => {
        shoppingListType = undefined;
      });
    }
  });

  it('ShoppingListTypes menu should load ShoppingListTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('shopping-list-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ShoppingListType').should('exist');
    cy.location('pathname').should('eq', shoppingListTypePageUrl);
  });

  describe('ShoppingListType page', () => {
    it('should have translated page title', () => {
      cy.visit(shoppingListTypePageUrl);
      cy.getEntityHeading('ShoppingListType').should('not.contain', 'snpApp.shoppingListType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(shoppingListTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ShoppingListType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${shoppingListTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ShoppingListType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shoppingListTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/shopping-list-types',
          body: shoppingListTypeSample,
        }).then(({ body }) => {
          shoppingListType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/shopping-list-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [shoppingListType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(shoppingListTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ShoppingListType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('shoppingListType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shoppingListTypePageUrl);
      });

      it('edit button click should load edit ShoppingListType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ShoppingListType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shoppingListTypePageUrl);
      });

      it('edit button click should load edit ShoppingListType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ShoppingListType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shoppingListTypePageUrl);
      });

      it('last delete button click should delete instance of ShoppingListType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('shoppingListType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shoppingListTypePageUrl);

        shoppingListType = undefined;
      });
    });
  });

  describe('new ShoppingListType page', () => {
    beforeEach(() => {
      cy.visit(shoppingListTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ShoppingListType');
    });

    it('should create an instance of ShoppingListType', () => {
      cy.get(`[data-cy="name"]`).type('after inquisitively along');
      cy.get(`[data-cy="name"]`).should('have.value', 'after inquisitively along');

      cy.get(`[data-cy="description"]`).type('after');
      cy.get(`[data-cy="description"]`).should('have.value', 'after');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        shoppingListType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', shoppingListTypePageUrl);
    });
  });
});
