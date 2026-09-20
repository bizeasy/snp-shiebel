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

describe('ShoppingList e2e test', () => {
  const shoppingListPageUrl = '/shopping-list';
  let username: string;
  let password: string;
  const shoppingListSample = { name: 'spiffy though unimportant' };

  let shoppingList;
  let party;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/parties',
      body: {
        isOrganisation: false,
        organisationName: 'long-term',
        organisationShortName: 'sans aboard amongst',
        salutation: 'amongst intrepid cul',
        firstName: 'Amari',
        middleName: 'trust powerful hm',
        lastName: 'Hamill',
        gender: 'OTHER',
        birthDate: '2023-12-14',
        primaryPhone: 'who patiently',
        primaryEmail: 'grouper yearly which',
        isTemporaryPassword: true,
        logoImageUrl: 'hm',
        profileImageUrl: 'antique',
        notes: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        postalAddress: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      party = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/shopping-lists+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/shopping-lists').as('postEntityRequest');
    cy.intercept('DELETE', '/api/shopping-lists/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/shopping-list-types', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/parties', {
      statusCode: 200,
      body: [party],
    });

    cy.intercept('GET', '/api/statuses', {
      statusCode: 200,
      body: [],
    });
  });

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

  afterEach(() => {
    if (party) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/parties/${party.id}`,
      }).then(() => {
        party = undefined;
      });
    }
  });

  it('ShoppingLists menu should load ShoppingLists page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('shopping-list');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ShoppingList').should('exist');
    cy.location('pathname').should('eq', shoppingListPageUrl);
  });

  describe('ShoppingList page', () => {
    it('should have translated page title', () => {
      cy.visit(shoppingListPageUrl);
      cy.getEntityHeading('ShoppingList').should('not.contain', 'snpApp.shoppingList.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(shoppingListPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ShoppingList page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${shoppingListPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ShoppingList');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shoppingListPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/shopping-lists',
          body: {
            ...shoppingListSample,
            party,
          },
        }).then(({ body }) => {
          shoppingList = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/shopping-lists+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/shopping-lists?page=0&size=20>; rel="last",<http://localhost/api/shopping-lists?page=0&size=20>; rel="first"',
              },
              body: [shoppingList],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(shoppingListPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ShoppingList page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('shoppingList');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shoppingListPageUrl);
      });

      it('edit button click should load edit ShoppingList page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ShoppingList');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shoppingListPageUrl);
      });

      it('edit button click should load edit ShoppingList page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ShoppingList');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shoppingListPageUrl);
      });

      it('last delete button click should delete instance of ShoppingList', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('shoppingList').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shoppingListPageUrl);

        shoppingList = undefined;
      });
    });
  });

  describe('new ShoppingList page', () => {
    beforeEach(() => {
      cy.visit(shoppingListPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ShoppingList');
    });

    it('should create an instance of ShoppingList', () => {
      cy.get(`[data-cy="name"]`).type('unlike edge when');
      cy.get(`[data-cy="name"]`).should('have.value', 'unlike edge when');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="party"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        shoppingList = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', shoppingListPageUrl);
    });
  });
});
