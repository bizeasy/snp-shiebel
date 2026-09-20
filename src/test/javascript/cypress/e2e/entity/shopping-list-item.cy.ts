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

describe('ShoppingListItem e2e test', () => {
  const shoppingListItemPageUrl = '/shopping-list-item';
  let username: string;
  let password: string;
  // const shoppingListItemSample = {"quantity":8082.54};

  let shoppingListItem;
  // let shoppingList;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/shopping-lists',
      body: {"name":"instead camouflage","isActive":true},
    }).then(({ body }) => {
      shoppingList = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/shopping-list-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/shopping-list-items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/shopping-list-items/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/shopping-lists', {
      statusCode: 200,
      body: [shoppingList],
    });

    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/statuses', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (shoppingListItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/shopping-list-items/${shoppingListItem.id}`,
      }).then(() => {
        shoppingListItem = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (shoppingList) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/shopping-lists/${shoppingList.id}`,
      }).then(() => {
        shoppingList = undefined;
      });
    }
  });
   */

  it('ShoppingListItems menu should load ShoppingListItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('shopping-list-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ShoppingListItem').should('exist');
    cy.location('pathname').should('eq', shoppingListItemPageUrl);
  });

  describe('ShoppingListItem page', () => {
    it('should have translated page title', () => {
      cy.visit(shoppingListItemPageUrl);
      cy.getEntityHeading('ShoppingListItem').should('not.contain', 'snpApp.shoppingListItem.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(shoppingListItemPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ShoppingListItem page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${shoppingListItemPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ShoppingListItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shoppingListItemPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/shopping-list-items',
          body: {
            ...shoppingListItemSample,
            shoppingList: shoppingList,
          },
        }).then(({ body }) => {
          shoppingListItem = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/shopping-list-items+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/shopping-list-items?page=0&size=20>; rel="last",<http://localhost/api/shopping-list-items?page=0&size=20>; rel="first"',
              },
              body: [shoppingListItem],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(shoppingListItemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(shoppingListItemPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details ShoppingListItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('shoppingListItem');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shoppingListItemPageUrl);
      });

      it('edit button click should load edit ShoppingListItem page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ShoppingListItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shoppingListItemPageUrl);
      });

      it('edit button click should load edit ShoppingListItem page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ShoppingListItem');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shoppingListItemPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of ShoppingListItem', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('shoppingListItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shoppingListItemPageUrl);

        shoppingListItem = undefined;
      });
    });
  });

  describe('new ShoppingListItem page', () => {
    beforeEach(() => {
      cy.visit(shoppingListItemPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ShoppingListItem');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of ShoppingListItem', () => {
      cy.get(`[data-cy="sequenceNo"]`).type('12716');
      cy.get(`[data-cy="sequenceNo"]`).should('have.value', '12716');

      cy.get(`[data-cy="quantity"]`).type('10949.25');
      cy.get(`[data-cy="quantity"]`).should('have.value', '10949.25');

      cy.get(`[data-cy="requiredByDate"]`).type('2023-12-14T19:42');
      cy.get(`[data-cy="requiredByDate"]`).blur();
      cy.get(`[data-cy="requiredByDate"]`).should('have.value', '2023-12-14T19:42');

      cy.get(`[data-cy="selectedAmount"]`).type('31282.9');
      cy.get(`[data-cy="selectedAmount"]`).should('have.value', '31282.9');

      cy.get(`[data-cy="maxAmount"]`).type('7300.53');
      cy.get(`[data-cy="maxAmount"]`).should('have.value', '7300.53');

      cy.get(`[data-cy="description"]`).type('brilliant');
      cy.get(`[data-cy="description"]`).should('have.value', 'brilliant');

      cy.get(`[data-cy="story"]`).type('lest');
      cy.get(`[data-cy="story"]`).should('have.value', 'lest');

      cy.get(`[data-cy="shoppingList"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        shoppingListItem = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', shoppingListItemPageUrl);
    });
  });
});
