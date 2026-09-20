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

describe('PickListItem e2e test', () => {
  const pickListItemPageUrl = '/pick-list-item';
  let username: string;
  let password: string;
  const pickListItemSample = { quantity: 1634.86 };

  let pickListItem;
  let pickList;

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
      url: '/api/pick-lists',
      body: { description: 'once suspiciously form', pickListDate: '2023-12-14T12:46:38.557Z' },
    }).then(({ body }) => {
      pickList = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/pick-list-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/pick-list-items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/pick-list-items/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/pick-lists', {
      statusCode: 200,
      body: [pickList],
    });

    cy.intercept('GET', '/api/orders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/order-items', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/inventory-items', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/facility-locations', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/statuses', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (pickListItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/pick-list-items/${pickListItem.id}`,
      }).then(() => {
        pickListItem = undefined;
      });
    }
  });

  afterEach(() => {
    if (pickList) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/pick-lists/${pickList.id}`,
      }).then(() => {
        pickList = undefined;
      });
    }
  });

  it('PickListItems menu should load PickListItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('pick-list-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PickListItem').should('exist');
    cy.location('pathname').should('eq', pickListItemPageUrl);
  });

  describe('PickListItem page', () => {
    it('should have translated page title', () => {
      cy.visit(pickListItemPageUrl);
      cy.getEntityHeading('PickListItem').should('not.contain', 'snpApp.pickListItem.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(pickListItemPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PickListItem page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${pickListItemPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('PickListItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', pickListItemPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/pick-list-items',
          body: {
            ...pickListItemSample,
            pickList,
          },
        }).then(({ body }) => {
          pickListItem = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/pick-list-items+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/pick-list-items?page=0&size=20>; rel="last",<http://localhost/api/pick-list-items?page=0&size=20>; rel="first"',
              },
              body: [pickListItem],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(pickListItemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PickListItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('pickListItem');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', pickListItemPageUrl);
      });

      it('edit button click should load edit PickListItem page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PickListItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', pickListItemPageUrl);
      });

      it('edit button click should load edit PickListItem page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PickListItem');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', pickListItemPageUrl);
      });

      it('last delete button click should delete instance of PickListItem', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('pickListItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', pickListItemPageUrl);

        pickListItem = undefined;
      });
    });
  });

  describe('new PickListItem page', () => {
    beforeEach(() => {
      cy.visit(pickListItemPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PickListItem');
    });

    it('should create an instance of PickListItem', () => {
      cy.get(`[data-cy="quantity"]`).type('22773.32');
      cy.get(`[data-cy="quantity"]`).should('have.value', '22773.32');

      cy.get(`[data-cy="pickList"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        pickListItem = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', pickListItemPageUrl);
    });
  });
});
